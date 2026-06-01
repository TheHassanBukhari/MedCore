#!/bin/bash

echo ""
echo "========================================="
echo "     MEDCORE HOSPITAL MANAGEMENT"
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
if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    echo ""
    echo "Error details:"
    echo "-----------------------------------------"
    cat "$ERROR_LOG"
    echo "-----------------------------------------"
    exit 1
fi

# Remove empty compile log after successful build
rm -f "$ERROR_LOG"
echo "Compilation successful!"
echo ""

# Ask user which version to run
echo "========================================="
echo "     SELECT APPLICATION VERSION"
echo "========================================="
echo ""
echo "1) Console Version"
echo "2) GUI Version"
echo ""
echo -n "Enter your choice (1 or 2): "
read choice

echo ""

case $choice in
    1)
        echo "========================================="
        echo "      STARTING CONSOLE APPLICATION"
        echo "========================================="
        echo ""
        java -cp "$OUT_DIR:$LIB_DIR/ojdbc11.jar" console.Main
        ;;
    2)
        echo "========================================="
        echo "        STARTING GUI APPLICATION"
        echo "========================================="
        echo ""
        java -cp "$OUT_DIR:$LIB_DIR/ojdbc11.jar" gui.Main
        ;;
    *)
        echo "Invalid choice! Please run the script again and select 1 or 2."
        exit 1
        ;;
esac
