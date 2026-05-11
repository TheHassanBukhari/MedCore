#!/bin/bash

echo ""
echo "========================================="
echo "     MEDCORE HOSPITAL MANAGEMENT"
echo "             GUI VERSION"
echo "========================================="
echo ""

# Project root directory
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Paths
OUT_DIR="$PROJECT_DIR/out"
SRC_DIR="$PROJECT_DIR/src"
LIB_DIR="$PROJECT_DIR/lib"
ERROR_LOG="$PROJECT_DIR/compile_error.log"

# Check JDBC driver
if [ ! -f "$LIB_DIR/ojdbc11.jar" ]; then
    echo "ERROR: ojdbc11.jar not found!"
    echo "Place it inside:"
    echo "$LIB_DIR"
    exit 1
fi

echo "Oracle JDBC driver found"

# Create output directory
mkdir -p "$OUT_DIR"

# Clean previous class files
echo ""
echo "Cleaning old class files..."
rm -rf "$OUT_DIR"/*

# Remove old compile log if exists
rm -f "$ERROR_LOG"

echo ""
echo "Compiling project..."
echo ""

# Compile all Java files
javac -cp "$LIB_DIR/ojdbc11.jar" -d "$OUT_DIR" \
    "$SRC_DIR/models/"*.java \
    "$SRC_DIR/db/"*.java \
    "$SRC_DIR/gui/"*.java \
    "$SRC_DIR/console/"*.java \
    2> "$ERROR_LOG"

# Check compilation status
if [ $? -eq 0 ]; then

    # Remove empty compile log after successful build
    rm -f "$ERROR_LOG"

    echo "Compilation successful!"
    echo ""

    echo "========================================="
    echo "        STARTING GUI APPLICATION"
    echo "========================================="
    echo ""

    java -cp "$OUT_DIR:$LIB_DIR/ojdbc11.jar" gui.Main

else
    echo "Compilation failed!"
    echo ""
    echo "Error details:"
    echo "-----------------------------------------"
    cat "$ERROR_LOG"
    echo "-----------------------------------------"
    exit 1
fi
