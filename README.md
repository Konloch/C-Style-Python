# C-Style Python
Acts similiar to [Bython](https://github.com/mathialo/bython).

## Features
+ Transpilers for: `Bython to Python`, `Python to C-Style Python`, `C-Style Python to Python`
+ CLI & Java API for compiling or interpret - interpreting requires python to be installed on the machine (version is up to the user).

## What Is C-Style Python
+ It's similar to Bython, with the major difference being `If statements`, `for loops` & `while loops` are C/Java like.
    + This means instead of using tabs, brackets define scope.
    + `///` defines a single-line comment, we also inherit python comments with `#`
    + `/*` starts a multi-line comment, it ends with `*/`
    + If / While / For / Try / Catch statements can optionally include parenthesis such as `if (a == 5)`
+ All syntax changes are optional, the transpiler supports python code by default, so you can keep your favorite python syntax sugars.

## How To Use
+ Edit the `config.ini` and provide your python paths for the version you want to use.
+ Run the class 'CStylePython' with the argument pointing to the script you want to interpret with the .cpy file extension

## Requirements
+ Java 1.8 or greater - for transpiling.
+ Python (any version) - if you want to interpret the C-Style Python code.

## Notes
+ Super experimental, lacks an AST - so it's prone to breaking, feel free to report any edge-cases you find!

### Python Code
```python
def demo():
    # Variable assignments
    x = 10
    y = 5

    # Conditional statement
    if x > y:
        print("x is greater than y")
    else:
        print("x is not greater than y")

    # Loop with error handling
    for i in range(5):
        try:
            result = x / (y - i)
            print(f"Result of division: {result}")
        except ZeroDivisionError:
            print("Division by zero occurred")

# Call the function
demo()
```

### C-Style Python Code
```python
def demo() {
    # Variable assignments
    x = 10;
    y = 5;

    # Conditional statement
    if (x > y) {
        print("x is greater than y");
    } else {
        print("x is not greater than y");
    }

    # Loop with error handling
    for (i in range(5)) {
        try {
            result = x / (y - i);
            print(f"Result of division: {result}");
        } catch (ZeroDivisionError) {
            print("Division by zero occurred");
        }
    }
}

# Call the function
demo();
```