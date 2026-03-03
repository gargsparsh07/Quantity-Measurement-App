📏 Quantity Measurement Application
📌 Overview

The Quantity Measurement Application is a generic, extensible measurement engine built using Java and Maven.

It supports:

Length measurement

Weight measurement

Volume measurement

Temperature measurement (with selective arithmetic support)

The system is designed using Generics, Enums, Functional Interfaces, Lambda Expressions, and SOLID principles to ensure scalability, type safety, and maintainability.

This project evolves through multiple use cases (UC1–UC14), progressively transforming from a simple equality check into a fully extensible architecture capable of handling non-linear conversions and category-specific operational constraints.

🏗️ Architecture Highlights
1️⃣ Generic Design

The core class:

Quantity<U extends IMeasurable>

This ensures:

Compile-time type safety

Prevention of cross-category mixing

Reusable arithmetic logic

Example:

Quantity<LengthUnit>
Quantity<WeightUnit>
Quantity<TemperatureUnit>
2️⃣ Measurement Categories
📐 Length

FEET

INCHES

YARD

Supports:

Equality

Conversion

Addition

Subtraction

Division

⚖️ Weight

KILOGRAM

GRAM

TONNE

Supports:

Equality

Conversion

Addition

Subtraction

Division

🧪 Volume

LITRE

MILLILITRE

GALLON

Supports:

Equality

Conversion

Addition

Subtraction

Division

🌡️ Temperature

CELSIUS

FAHRENHEIT

KELVIN

Supports:

Equality

Conversion

Does NOT support:

Addition

Subtraction

Division

Because absolute temperatures cannot be meaningfully added or divided.

🔥 Key Technical Concepts Used
✅ Generics

Ensures type safety:

Temperature cannot be compared with Length.

Weight cannot be added to Volume.

✅ Functional Interfaces

Used:

DoubleBinaryOperator

Custom SupportsArithmetic

Allows clean lambda-based logic.

✅ Lambda Expressions

Example:

(a, b) -> a + b

Used for:

Arithmetic operations

Temperature conversion logic

✅ Enum-Based Behavior

Each unit type encapsulates its own conversion logic.

No if-else chains.

Clean polymorphism.

✅ Interface Segregation Principle (ISP)

IMeasurable evolved to support:

Default methods

Optional arithmetic validation

Capability-based design

Temperature overrides arithmetic support while other categories inherit default behavior.

✅ Non-Linear Conversion Support

Unlike length/weight/volume (linear factor-based conversion), temperature uses formulas:

°F = (°C × 9/5) + 32

°C = (°F − 32) × 5/9

K = °C + 273.15

The system supports both linear and non-linear unit systems seamlessly.

🧠 Capability-Based Design

Each unit defines whether it supports arithmetic operations.

supportsArithmetic()
validateOperationSupport(String operation)

This allows:

Graceful runtime validation

Clear error messages

Backward compatibility

Future extensibility

🛡️ Type Safety & Cross-Category Protection

The system prevents:

new Quantity<>(100, CELSIUS)
    .equals(new Quantity<>(100, FEET))

This returns false.

Generics + runtime validation provide layered protection.

🧪 Testing Coverage

Comprehensive JUnit testing includes:

Equality checks (same unit and cross-unit)

Conversion accuracy

Non-linear temperature conversion

Unsupported operation validation

Cross-category prevention

Edge cases (absolute zero, -40 intersection)

Symmetry and transitivity validation

Epsilon-based floating point precision handling

All UC1–UC14 test cases pass without breaking previous functionality.

🏗️ Project Structure
src/main/java/com/bridgelabz
│
├── Quantity.java
├── IMeasurable.java
├── SupportsArithmetic.java
├── LengthUnit.java
├── WeightUnit.java
├── VolumeUnit.java
├── TemperatureUnit.java
└── QuantityMeasurementApp.java

src/test/java/com/bridgelabz
│
├── QuantityMeasurementAppTest.java
└── TemperatureTest.java
🚀 Features Summary

✔ Generic measurement engine
✔ Multi-category support
✔ Linear & non-linear conversions
✔ Selective arithmetic support
✔ Functional programming integration
✔ SOLID-compliant architecture
✔ Clean separation of concerns
✔ Fully extensible for future categories

📚 Concepts Demonstrated

Functional Interfaces

Lambda Expressions

Enum Polymorphism

Default Interface Methods

Interface Evolution

Interface Segregation Principle

Capability-Based Design

Type Safety with Generics

Non-Linear Mathematical Conversion

Exception Semantics

Epsilon-Based Floating Point Comparison

🏁 Conclusion

This project demonstrates how a simple measurement equality system can evolve into a scalable, extensible, type-safe architecture capable of handling diverse measurement systems with different operational constraints.

It serves as a practical demonstration of advanced Java concepts and clean software design principles.
