package ua.edu.chnu.kkn.lab6.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VectorUInt Tests")
class VectorUIntTest {
    private VectorUInt vector;

    @BeforeEach
    void setUp() {
        vector = new VectorUInt(3, 5);
    }

    // ==================== ТЕСТИ КОНСТРУКТОРІВ ====================

    @Test
    @DisplayName("Default constructor should create vector of size 1 with value 0")
    void testDefaultConstructor() {
        VectorUInt v = new VectorUInt();
        assertEquals(1, v.size());
        assertEquals(0, v.get(0));
    }

    @Test
    @DisplayName("Constructor with size should create vector with zeros")
    void testSizeConstructor() {
        VectorUInt v = new VectorUInt(5);
        assertEquals(5, v.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(0, v.get(i));
        }
    }

    @Test
    @DisplayName("Constructor with size and init value should set all elements")
    void testSizeAndValueConstructor() {
        VectorUInt v = new VectorUInt(4, 10);
        assertEquals(4, v.size());
        for (int i = 0; i < 4; i++) {
            assertEquals(10, v.get(i));
        }
    }

    @Test
    @DisplayName("Copy constructor should create identical vector")
    void testCopyConstructor() {
        VectorUInt copy = new VectorUInt(vector);
        assertEquals(vector.size(), copy.size());
        for (int i = 0; i < vector.size(); i++) {
            assertEquals(vector.get(i), copy.get(i));
        }
    }

    @Test
    @DisplayName("Constructor with size 0 should create vector of size 1")
    void testZeroSizeConstructor() {
        VectorUInt v = new VectorUInt(0);
        assertEquals(1, v.size());
    }

    // ==================== ТЕСТИ БАЗОВИХ МЕТОДІВ ====================

    @Test
    @DisplayName("size() should return correct size")
    void testSize() {
        assertEquals(3, vector.size());
    }

    @Test
    @DisplayName("get() should return correct value for valid index")
    void testGetValidIndex() {
        assertEquals(5, vector.get(0));
        assertEquals(5, vector.get(1));
        assertEquals(5, vector.get(2));
        assertEquals(0, vector.getCodeError());
    }

    @Test
    @DisplayName("get() should return 0 and set codeError for invalid index")
    void testGetInvalidIndex() {
        assertEquals(0, vector.get(-1));
        assertEquals(1, vector.getCodeError());

        assertEquals(0, vector.get(10));
        assertEquals(1, vector.getCodeError());
    }

    @Test
    @DisplayName("set() should set correct value for valid index")
    void testSetValidIndex() {
        vector.set(1, 42);
        assertEquals(42, vector.get(1));
        assertEquals(0, vector.getCodeError());
    }

    @Test
    @DisplayName("set() should set codeError for invalid index")
    void testSetInvalidIndex() {
        vector.set(-1, 100);
        assertEquals(1, vector.getCodeError());

        vector.set(10, 100);
        assertEquals(1, vector.getCodeError());
    }

    @Test
    @DisplayName("setAll() should set all elements to given value")
    void testSetAll() {
        vector.setAll(7);
        for (int i = 0; i < vector.size(); i++) {
            assertEquals(7, vector.get(i));
        }
        assertEquals(0, vector.getCodeError());
    }

    @Test
    @DisplayName("getNumVec() should return correct number of created vectors")
    void testGetNumVec() {
        int before = VectorUInt.getNumVec();
        new VectorUInt();
        new VectorUInt(3);
        assertEquals(before + 2, VectorUInt.getNumVec());
    }

    @Test
    @DisplayName("toString() should return properly formatted string")
    void testToString() {
        String str = vector.toString();
        assertTrue(str.contains("VectorUInt[3]"));
        assertTrue(str.contains("[5, 5, 5]"));
    }

    // ==================== ТЕСТИ УНАРНИХ ОПЕРАТОРІВ ====================

    @Test
    @DisplayName("increment() should increase all elements by 1")
    void testIncrement() {
        VectorUInt result = vector.increment();
        assertEquals(6, result.get(0));
        assertEquals(6, result.get(1));
        assertEquals(6, result.get(2));
        // Original should not change
        assertEquals(5, vector.get(0));
    }

    @Test
    @DisplayName("increment() should handle overflow")
    void testIncrementOverflow() {
        VectorUInt v = new VectorUInt(1, 0xFFFFFFFF);
        VectorUInt result = v.increment();
        assertEquals(0, result.get(0));
    }

    @Test
    @DisplayName("decrement() should decrease all elements by 1")
    void testDecrement() {
        VectorUInt result = vector.decrement();
        assertEquals(4, result.get(0));
        assertEquals(4, result.get(1));
        assertEquals(4, result.get(2));
    }

    @Test
    @DisplayName("decrement() should not go below 0")
    void testDecrementZero() {
        VectorUInt v = new VectorUInt(3, 0);
        VectorUInt result = v.decrement();
        assertEquals(0, result.get(0));
        assertEquals(0, result.get(1));
        assertEquals(0, result.get(2));
    }

    @Test
    @DisplayName("bitwiseNot() should perform bitwise NOT on all elements")
    void testBitwiseNot() {
        VectorUInt v = new VectorUInt(2, 0);
        VectorUInt result = v.bitwiseNot();
        assertEquals(0xFFFFFFFFL, result.getUnsigned(0));
        assertEquals(0xFFFFFFFFL, result.getUnsigned(1));
    }

    @Test
    @DisplayName("isEmpty() should return true only for zero size")
    void testIsEmpty() {
        assertFalse(vector.isEmpty());
        VectorUInt empty = new VectorUInt(0);
        assertFalse(empty.isEmpty()); // size becomes 1
    }

    @Test
    @DisplayName("isTrue() should return true if any element is non-zero")
    void testIsTrue() {
        assertTrue(vector.isTrue());

        VectorUInt zero = new VectorUInt(3, 0);
        assertFalse(zero.isTrue());

        vector.set(1, 0);
        assertTrue(vector.isTrue()); // still has non-zero elements
    }

    @Test
    @DisplayName("isFalse() should return true if all elements are zero")
    void testIsFalse() {
        assertFalse(vector.isFalse());

        VectorUInt zero = new VectorUInt(3, 0);
        assertTrue(zero.isFalse());
    }

    // ==================== ТЕСТИ АРИФМЕТИЧНИХ ОПЕРАТОРІВ ====================

    @Test
    @DisplayName("add() with another vector should add corresponding elements")
    void testAddVector() {
        VectorUInt other = new VectorUInt(3, 2);
        VectorUInt result = vector.add(other);
        assertEquals(7, result.get(0));
        assertEquals(7, result.get(1));
        assertEquals(7, result.get(2));
    }

    @Test
    @DisplayName("add() with different sizes should handle correctly")
    void testAddDifferentSizes() {
        VectorUInt v1 = new VectorUInt(5, 10);
        VectorUInt v2 = new VectorUInt(3, 5);
        VectorUInt result = v1.add(v2);
        assertEquals(5, result.size());
        assertEquals(15, result.get(0));
        assertEquals(15, result.get(1));
        assertEquals(15, result.get(2));
        assertEquals(10, result.get(3));
        assertEquals(10, result.get(4));
    }

    @Test
    @DisplayName("add() with scalar should add scalar to all elements")
    void testAddScalar() {
        VectorUInt result = vector.add(3);
        assertEquals(8, result.get(0));
        assertEquals(8, result.get(1));
        assertEquals(8, result.get(2));
    }

    @Test
    @DisplayName("subtract() with another vector should subtract corresponding elements")
    void testSubtractVector() {
        VectorUInt other = new VectorUInt(3, 2);
        VectorUInt result = vector.subtract(other);
        assertEquals(3, result.get(0));
        assertEquals(3, result.get(1));
        assertEquals(3, result.get(2));
    }

    @Test
    @DisplayName("subtract() should not go below 0")
    void testSubtractUnderflow() {
        VectorUInt other = new VectorUInt(3, 10);
        VectorUInt result = vector.subtract(other);
        assertEquals(0, result.get(0));
        assertEquals(0, result.get(1));
        assertEquals(0, result.get(2));
    }

    @Test
    @DisplayName("subtract() with scalar should subtract from all elements")
    void testSubtractScalar() {
        VectorUInt result = vector.subtract(3);
        assertEquals(2, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals(2, result.get(2));
    }

    @Test
    @DisplayName("multiply() with another vector should multiply corresponding elements")
    void testMultiplyVector() {
        VectorUInt other = new VectorUInt(3, 2);
        VectorUInt result = vector.multiply(other);
        assertEquals(10, result.get(0));
        assertEquals(10, result.get(1));
        assertEquals(10, result.get(2));
    }

    @Test
    @DisplayName("multiply() should handle overflow")
    void testMultiplyOverflow() {
        VectorUInt v1 = new VectorUInt(1, 0x10000);
        VectorUInt v2 = new VectorUInt(1, 0x10000);
        VectorUInt result = v1.multiply(v2);
        assertEquals(0xFFFFFFFFL, result.getUnsigned(0));
    }

    @Test
    @DisplayName("multiply() with scalar should multiply all elements")
    void testMultiplyScalar() {
        VectorUInt result = vector.multiply(3);
        assertEquals(15, result.get(0));
        assertEquals(15, result.get(1));
        assertEquals(15, result.get(2));
    }

    @Test
    @DisplayName("divide() with another vector should divide corresponding elements")
    void testDivideVector() {
        VectorUInt other = new VectorUInt(3, 2);
        VectorUInt result = vector.divide(other);
        assertEquals(2, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals(2, result.get(2));
    }

    @Test
    @DisplayName("divide() by zero should return 0")
    void testDivideByZero() {
        VectorUInt other = new VectorUInt(3, 0);
        VectorUInt result = vector.divide(other);
        assertEquals(0, result.get(0));
    }

    @Test
    @DisplayName("modulo() with another vector should compute remainder")
    void testModuloVector() {
        VectorUInt other = new VectorUInt(3, 2);
        VectorUInt result = vector.modulo(other);
        assertEquals(1, result.get(0));
        assertEquals(1, result.get(1));
        assertEquals(1, result.get(2));
    }

    // ==================== ТЕСТИ ПОБІТОВИХ ОПЕРАТОРІВ ====================

    @Test
    @DisplayName("bitwiseOr() should perform bitwise OR")
    void testBitwiseOr() {
        VectorUInt v1 = new VectorUInt(2, 0x0F);
        VectorUInt v2 = new VectorUInt(2, 0xF0);
        VectorUInt result = v1.bitwiseOr(v2);
        assertEquals(0xFF, result.getUnsigned(0));
    }

    @Test
    @DisplayName("bitwiseXor() should perform bitwise XOR")
    void testBitwiseXor() {
        VectorUInt v1 = new VectorUInt(2, 0xFF);
        VectorUInt v2 = new VectorUInt(2, 0x0F);
        VectorUInt result = v1.bitwiseXor(v2);
        assertEquals(0xF0, result.getUnsigned(0));
    }

    @Test
    @DisplayName("bitwiseAnd() should perform bitwise AND")
    void testBitwiseAnd() {
        VectorUInt v1 = new VectorUInt(2, 0xFF);
        VectorUInt v2 = new VectorUInt(2, 0x0F);
        VectorUInt result = v1.bitwiseAnd(v2);
        assertEquals(0x0F, result.getUnsigned(0));
    }

    @Test
    @DisplayName("rightShift() should shift bits right")
    void testRightShift() {
        VectorUInt v = new VectorUInt(2, 8);
        VectorUInt result = v.rightShift(2);
        assertEquals(2, result.get(0));
    }

    @Test
    @DisplayName("leftShift() should shift bits left")
    void testLeftShift() {
        VectorUInt v = new VectorUInt(2, 2);
        VectorUInt result = v.leftShift(2);
        assertEquals(8, result.get(0));
    }

    // ==================== ТЕСТИ ОПЕРАТОРІВ ПОРІВНЯННЯ ====================

    @Test
    @DisplayName("equals() should return true for identical vectors")
    void testEquals() {
        VectorUInt same = new VectorUInt(3, 5);
        VectorUInt different = new VectorUInt(3, 10);
        VectorUInt differentSize = new VectorUInt(4, 5);

        assertTrue(vector.equals(same));
        assertFalse(vector.equals(different));
        assertFalse(vector.equals(differentSize));
        assertFalse(vector.equals(null));
        assertTrue(vector.equals(vector));
    }

    @Test
    @DisplayName("notEquals() should return opposite of equals()")
    void testNotEquals() {
        VectorUInt other = new VectorUInt(3, 10);
        assertTrue(vector.notEquals(other));
        assertFalse(vector.notEquals(new VectorUInt(3, 5)));
    }

    @Test
    @DisplayName("greaterThan() should compare all elements")
    void testGreaterThan() {
        VectorUInt smaller = new VectorUInt(3, 3);
        VectorUInt equal = new VectorUInt(3, 5);
        VectorUInt larger = new VectorUInt(3, 7);

        assertTrue(vector.greaterThan(smaller));
        assertFalse(vector.greaterThan(equal));
        assertFalse(vector.greaterThan(larger));
    }

    @Test
    @DisplayName("greaterOrEqual() should compare all elements")
    void testGreaterOrEqual() {
        VectorUInt smaller = new VectorUInt(3, 3);
        VectorUInt equal = new VectorUInt(3, 5);

        assertTrue(vector.greaterOrEqual(smaller));
        assertTrue(vector.greaterOrEqual(equal));
    }

    @Test
    @DisplayName("lessThan() should compare all elements")
    void testLessThan() {
        VectorUInt smaller = new VectorUInt(3, 3);
        VectorUInt larger = new VectorUInt(3, 7);

        assertTrue(vector.lessThan(larger));
        assertFalse(vector.lessThan(smaller));
    }

    @Test
    @DisplayName("lessOrEqual() should compare all elements")
    void testLessOrEqual() {
        VectorUInt larger = new VectorUInt(3, 7);
        VectorUInt equal = new VectorUInt(3, 5);

        assertTrue(vector.lessOrEqual(larger));
        assertTrue(vector.lessOrEqual(equal));
    }

    // ==================== ТЕСТИ ДОДАТКОВИХ МЕТОДІВ ====================

    @Test
    @DisplayName("max() should return maximum element")
    void testMax() {
        vector.set(0, 10);
        vector.set(1, 3);
        vector.set(2, 7);
        assertEquals(10, vector.max());
    }

    @Test
    @DisplayName("min() should return minimum element")
    void testMin() {
        vector.set(0, 10);
        vector.set(1, 3);
        vector.set(2, 7);
        assertEquals(3, vector.min());
    }

    @Test
    @DisplayName("sum() should return sum of all elements")
    void testSum() {
        assertEquals(15, vector.sum());

        vector.set(0, 10);
        vector.set(1, 20);
        vector.set(2, 30);
        assertEquals(60, vector.sum());
    }

    @Test
    @DisplayName("average() should return arithmetic mean")
    void testAverage() {
        assertEquals(5.0, vector.average(), 0.001);

        vector.setAll(10);
        assertEquals(10.0, vector.average(), 0.001);
    }

    @Test
    @DisplayName("sorted() should return sorted vector")
    void testSorted() {
        vector.set(0, 30);
        vector.set(1, 10);
        vector.set(2, 20);

        VectorUInt sorted = vector.sorted();
        assertEquals(10, sorted.get(0));
        assertEquals(20, sorted.get(1));
        assertEquals(30, sorted.get(2));

        // Original should not change
        assertEquals(30, vector.get(0));
    }

    @Test
    @DisplayName("reversed() should return reversed vector")
    void testReversed() {
        vector.set(0, 10);
        vector.set(1, 20);
        vector.set(2, 30);

        VectorUInt reversed = vector.reversed();
        assertEquals(30, reversed.get(0));
        assertEquals(20, reversed.get(1));
        assertEquals(10, reversed.get(2));
    }

    @Test
    @DisplayName("isAllZero() should return true only if all elements are zero")
    void testIsAllZero() {
        assertFalse(vector.isAllZero());

        VectorUInt zero = new VectorUInt(3, 0);
        assertTrue(zero.isAllZero());
    }

    @Test
    @DisplayName("countNonZero() should return number of non-zero elements")
    void testCountNonZero() {
        assertEquals(3, vector.countNonZero());

        vector.set(1, 0);
        assertEquals(2, vector.countNonZero());

        vector.setAll(0);
        assertEquals(0, vector.countNonZero());
    }

    @Test
    @DisplayName("hashCode() should be consistent with equals()")
    void testHashCode() {
        VectorUInt v1 = new VectorUInt(3, 5);
        VectorUInt v2 = new VectorUInt(3, 5);

        assertEquals(v1.hashCode(), v2.hashCode());
    }
}