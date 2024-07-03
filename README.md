# C-Style Python
Acts similiar to [Bython](https://github.com/mathialo/bython).

## Features
+ Transpilers for: `Bython to Python`, `Python to C-Style Python`, `C-Style Python to Python`
+ CLI & Java API for compiling or running, running requires python (version is up to the user).

## What Is C-Style Python
+ It's similar to Bython, with the major difference being `If statements`, `for loops` & `while loops` are C/Java like.
    + This means instead of using tabs, brackets define scope.
    + `//` defines a comment, we also inherit python comments with `#`
    + If / While / For / Try / Catch statements can optionally include parenthesis such as `if (a == 5)`
+ All syntax changes are optional, the transpiler supports python code by default, so you can keep your favorite python syntax sugars.

## How To Use
+ Edit the `config.ini` and provide your python paths for the version you want to use.
+ Run the class 'CStylePython' with the argument pointing to the script you want to run with the .bpp file extension

## Requirements
+ Java 1.8 or greater
+ Python (any version) if you want to run the bpp code.

## Notes
+ Super experimental, lacks an AST - so it's prone to breaking, feel free to report any edge-cases you find!