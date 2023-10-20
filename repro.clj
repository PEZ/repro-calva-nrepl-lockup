;; Don't connect the REPL just yet.
;; Repro steps starting on line 30 below.

(ns repro
  (:require
   [clojure.string :as str])
  (:refer-clojure :exclude [char]))

 (def ignr #"(?x)
             (?:                    # Ignorables
               \#\!.*\n? |            # hashbang line
               [\s,]+    |            # whitespace, commas,
               ;.*\n?                 # comments
             )")
 (def char #"\\.")                  ; Character token
 (def keyw #"(?:\:\w+(?:-\w+)*)")   ; Keyword token
 (def oper #"[-+*/<=>|&]{1,3}")     ; Operator token
 (def strg #"(?x)
             \#?                    # Possibly a regex
             \"(?:                  # Quoted string
               \\. |                  # Escaped char
               [^\\\"]                # Any other char
             )*\"?                    # Ending quote
             ")
 (def symb #"\w+(?:-\w+)*[?!]?")    ; Symbol token
 (def symp #"\w+(?:-\w+)*[?!]?\(")  ; Symbol followed by paren

 (def dict
   {:ignr ign
;; On ^that^ line:
;; 1. continue typing one letter after the word starting with `ign`
;; 2. hit backspace (assuming you have strict structural delete enabled)
;; 3. the letter is deleted.
;;    Try typing some more and backspace over it.
;;    It works.
;; 4. Start and connect the REPL
;; 5. Goto 1.
;; 6. Instead of 3. experience lockup,
;;    The letter will eventually be deleted

          (?:                      # Ignorables
            \#\!.*\n? |              # hashbang line
            [\s,]+    |              # whitespace, commas,
            ;.*\n?                   # comments
          )"
    :char #"\\."                   ; Character token
    :keyw #"(?:\:\w+(?:-\w+)*)"    ; Keyword token
    :oper #"[-+*/<=>|&]{1,3}"      ; Operator token
    :str  #"(?x)
          \#?                      # Possibly a regex
          \"(?:                    # Quoted string
            \\. |                    # Escaped char
            [^\\\"]                  # Any other char
          )*\"?                      # Ending quote
          "
    :sym  #"\w+(?:-\w+)*[?!]?"     ; Symbol token
    :symp #"\w+(?:-\w+)*[?!]?\("   ; Symbol followed by paren
    })

 (defn re [s]
   (re-pattern
     (reduce
       (fn [re [k v]]
         (let [pat (re-pattern (str #"\$" (subs (str k) 1) #"(?!\w)"))]
           (str/replace re pat (str/re-quote-replacement v))))
       s dict)))

 (comment)
