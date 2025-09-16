# Git Training CLI - Enterprise Architecture Documentation

## Project Structure Overview

This project follows Google-level enterprise architecture patterns with clear separation of concerns and professional organization.

### Directory Structure

```
cs-25-2-team3/
├── apps/                          # Application binaries and deployable artifacts
│   └── git-training-cli/          # Main CLI application
│       └── GitTrainingCLIEnterprise.java
├── java-src/                     # Modular enterprise Java source code
│   ├── achievements/              # Achievement and badge management
│   ├── cli/                      # Command-line interface components
│   ├── core/                     # Core application logic
│   ├── modules/                  # Learning module implementations
│   ├── persistence/              # Data persistence and session management
│   ├── ui/                       # User interface and animation components
│   └── utils/                    # Utility classes and helpers
├── src/                          # Node.js/JavaScript source (if applicable)
├── docs/                         # All documentation and specifications
├── scripts/                      # Build and deployment scripts
├── build-artifacts/              # Compiled binaries and build cache
├── archive/                      # Legacy code and historical versions
│   ├── legacy-monolith/          # Original monolithic application
│   └── legacy-modules/           # Original modular components
└── node_modules/                 # Node.js dependencies (if applicable)
```

## Architecture Principles

### 1. **Separation of Concerns**
- Each directory has a single, well-defined responsibility
- Clear boundaries between UI, business logic, and data persistence
- Modular design enabling independent development and testing

### 2. **Enterprise Patterns**
- **Repository Pattern**: Data access abstraction in `persistence/`
- **Service Layer**: Business logic separation in `core/`
- **Factory Pattern**: Module creation in `modules/`
- **Strategy Pattern**: Achievement calculations in `achievements/`

### 3. **Professional Organization**
- **Version Control**: Legacy code preserved in `archive/`
- **Build Management**: All artifacts isolated in `build-artifacts/`
- **Documentation**: Centralized in `docs/`
- **Scripts**: Deployment automation in `scripts/`

## Development Workflow

### Building the Application
```bash
# From project root
./scripts/build.sh
```

### Running the Enterprise CLI
```bash
# Navigate to application directory
cd apps/git-training-cli/
java GitTrainingCLIEnterprise
```

### Working with Modular Components
```bash
# Compile modular architecture
cd java-src/
javac -d ../build-artifacts/ **/*.java
```

## Code Quality Standards

- **Documentation**: All classes include comprehensive JavaDoc
- **Error Handling**: Enterprise-grade exception management
- **Testing**: Unit tests in corresponding test directories
- **Logging**: Structured logging for production monitoring
- **Performance**: Optimized for scalability and maintainability

## 🚀 Deployment Strategy

1. **Development**: Use modular source in `java-src/`
2. **Testing**: Compile to `build-artifacts/`
3. **Production**: Deploy from `apps/` directory
4. **Rollback**: Fallback to `archive/legacy-monolith/`

## 📈 Evolution Path

- **Phase 1**: Monolithic application (archived)
- **Phase 2**: Modular refactoring (current in `java-src/`)
- **Phase 3**: Enterprise single-file (current in `apps/`)
- **Phase 4**: Microservices architecture (future)

## 🔍 File Location Guide

### Need to find...
- **Main Application**: `apps/git-training-cli/GitTrainingCLIEnterprise.java`
- **Original Code**: `archive/legacy-monolith/GitTrainingCLI.java`
- **Modular Components**: `java-src/[component-type]/`
- **Build Scripts**: `scripts/build.sh`
- **Documentation**: `docs/[topic].md`
- **Compiled Code**: `build-artifacts/`

This structure enables efficient development, easy maintenance, and professional deployment practices following Google's internal engineering standards.
