# Calva, nREPL lockup repro

Following the instructions in [repro.clj](repro.clj) it seems that Calva sends something funny to the nREPL server in a `complete` message. It makes nREPL lock up. On some machines, fans start spinning and stuff.