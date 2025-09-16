#!/bin/bash

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BUILD_DIR="$PROJECT_ROOT/build-artifacts"
APP_DIR="$PROJECT_ROOT/apps/git-training-cli"
JAVA_SRC_DIR="$PROJECT_ROOT/java-src"

echo -e "${CYAN}Git Training CLI Enterprise Build System${NC}"
echo -e "${CYAN}========================================${NC}"
echo ""

print_status() {
    echo -e "${BLUE}$1${NC}"
}

print_success() {
    echo -e "${GREEN}$1${NC}"
}

print_error() {
    echo -e "${RED}$1${NC}"
}

print_warning() {
    echo -e "${YELLOW}$1${NC}"
}

print_status "Checking Java installation..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n1 | cut -d'"' -f2)
    print_success "Java found: $JAVA_VERSION"
else
    print_error "Java not found! Please install Java 8 or higher."
    exit 1
fi

print_status "Setting up build environment..."
mkdir -p "$BUILD_DIR"
print_success "Build directory ready: $BUILD_DIR"

echo ""
echo -e "${YELLOW}Build Options:${NC}"
echo "1. Enterprise Application (Single File)"
echo "2. Modular Architecture (Full Enterprise)"
echo "3. Both Applications"
echo "4. Clean Build Artifacts"
echo ""

read -p "Choose build option (1-4): " BUILD_OPTION

case $BUILD_OPTION in
    1)
        print_status "Building Enterprise Application..."
        cd "$APP_DIR"
        if javac GitTrainingCLIEnterprise.java; then
            print_success "Enterprise application compiled successfully!"
            echo ""
            echo -e "${GREEN}Ready to run:${NC}"
            echo -e "${CYAN}   cd apps/git-training-cli/${NC}"
            echo -e "${CYAN}   java GitTrainingCLIEnterprise${NC}"
        else
            print_error "Compilation failed!"
            exit 1
        fi
        ;;
    2)
        print_status "Building Modular Architecture..."
        cd "$JAVA_SRC_DIR"
        if find . -name "*.java" -exec javac -d "$BUILD_DIR" {} +; then
            print_success "Modular architecture compiled successfully!"
            echo ""
            echo -e "${GREEN}Ready to run:${NC}"
            echo -e "${CYAN}   cd build-artifacts/${NC}"
            echo -e "${CYAN}   java CommandLineInterface${NC}"
        else
            print_error "Modular compilation failed!"
            exit 1
        fi
        ;;
    3)
        print_status "Building both applications..."
        
        cd "$APP_DIR"
        if javac GitTrainingCLIEnterprise.java; then
            print_success "Enterprise application compiled!"
        else
            print_error "Enterprise application compilation failed!"
            exit 1
        fi
        
        cd "$JAVA_SRC_DIR"
        if find . -name "*.java" -exec javac -d "$BUILD_DIR" {} +; then
            print_success "Modular architecture compiled!"
        else
            print_error "Modular compilation failed!"
            exit 1
        fi
        
        print_success "Both applications built successfully!"
        ;;
    4)
        print_status "Cleaning build artifacts..."
        rm -rf "$BUILD_DIR"/*
        rm -f "$APP_DIR"/*.class
        find "$JAVA_SRC_DIR" -name "*.class" -delete 2>/dev/null || true
        print_success "Build artifacts cleaned!"
        ;;
    *)
        print_error "Invalid option selected!"
        exit 1
        ;;
esac

echo ""
echo -e "${GREEN}Build process completed successfully!${NC}"
echo -e "${BLUE}For more information, see docs/QUICKSTART.md${NC}"
