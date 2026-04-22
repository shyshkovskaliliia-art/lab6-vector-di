package ua.edu.chnu.kkn.lab6.model;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class VectorUInt {
    private final int[] array;
    private int codeError;
    private static int numVec = 0;


    public VectorUInt() {
        this(1, 0);
    }

    public VectorUInt(int size) {
        this(size, 0);
    }

    public VectorUInt(int size, int initValue) {
        this.array = new int[size > 0 ? size : 1];
        Arrays.fill(array, initValue & 0xFFFFFFFF);
        this.codeError = 0;
        numVec++;
    }

    // Конструктор копіювання
    public VectorUInt(VectorUInt other) {
        this.array = Arrays.copyOf(other.array, other.array.length);
        this.codeError = other.codeError;
        numVec++;
    }

    // ==================== БАЗОВІ МЕТОДИ ====================

    public int size() {
        return array.length;
    }

    public int getCodeError() {
        return codeError;
    }

    public void setCodeError(int codeError) {
        this.codeError = codeError;
    }

    public int get(int index) {
        if (index >= 0 && index < array.length) {
            codeError = 0;
            return array[index];
        } else {
            codeError = 1;
            return 0;
        }
    }

    public void set(int index, int value) {
        if (index >= 0 && index < array.length) {
            codeError = 0;
            array[index] = value & 0xFFFFFFFF;
        } else {
            codeError = 1;
        }
    }

    public void setAll(int value) {
        Arrays.fill(array, value & 0xFFFFFFFF);
        codeError = 0;
    }

    public static int getNumVec() {
        return numVec;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("VectorUInt[");
        sb.append(array.length).append("]: [");
        for (int i = 0; i < array.length; i++) {
            sb.append(toUnsignedString(array[i]));
            if (i < array.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    private String toUnsignedString(int value) {
        return Long.toString(value & 0xFFFFFFFFL);
    }

    public long getUnsigned(int index) {
        return get(index) & 0xFFFFFFFFL;
    }

    public int[] toArray() {
        return Arrays.copyOf(array, array.length);
    }

    // ==================== УНАРНІ ОПЕРАТОРИ ====================

    // ++ : збільшує всі елементи на 1
    public VectorUInt increment() {
        VectorUInt result = new VectorUInt(this);
        for (int i = 0; i < array.length; i++) {
            long val = (array[i] & 0xFFFFFFFFL) + 1;
            result.array[i] = val > 0xFFFFFFFFL ? 0 : (int) val;
        }
        return result;
    }

    // -- : зменшує всі елементи на 1 (не менше 0)
    public VectorUInt decrement() {
        VectorUInt result = new VectorUInt(this);
        for (int i = 0; i < array.length; i++) {
            long val = array[i] & 0xFFFFFFFFL;
            result.array[i] = val > 0 ? (int) (val - 1) : 0;
        }
        return result;
    }

    // ~ : побітове заперечення
    public VectorUInt bitwiseNot() {
        VectorUInt result = new VectorUInt(array.length);
        for (int i = 0; i < array.length; i++) {
            result.array[i] = ~array[i];
        }
        return result;
    }

    // ! : true якщо size == 0
    public boolean isEmpty() {
        return array.length == 0;
    }

    // true/false : true якщо size != 0 і хоча б один елемент != 0
    public boolean isTrue() {
        if (array.length == 0) return false;
        for (int val : array) {
            if (val != 0) return true;
        }
        return false;
    }

    public boolean isFalse() {
        return !isTrue();
    }

    // ==================== ДОПОМІЖНІ МЕТОДИ ДЛЯ БІНАРНИХ ОПЕРАЦІЙ ====================

    private static VectorUInt applyBinary(VectorUInt v1, VectorUInt v2,
                                          BinaryOperator<Long> operation) {
        int maxSize = Math.max(v1.array.length, v2.array.length);
        int minSize = Math.min(v1.array.length, v2.array.length);

        VectorUInt result = new VectorUInt(maxSize);

        for (int i = 0; i < minSize; i++) {
            long a = v1.array[i] & 0xFFFFFFFFL;
            long b = v2.array[i] & 0xFFFFFFFFL;
            long res = operation.apply(a, b);
            result.array[i] = (int) (res & 0xFFFFFFFFL);
        }

        // Додаткові елементи з більшого вектора
        if (v1.array.length > v2.array.length) {
            for (int i = minSize; i < maxSize; i++) {
                result.array[i] = v1.array[i];
            }
        } else if (v2.array.length > v1.array.length) {
            for (int i = minSize; i < maxSize; i++) {
                result.array[i] = v2.array[i];
            }
        }

        return result;
    }

    private static boolean compareVectors(VectorUInt v1, VectorUInt v2,
                                          BiFunction<Long, Long, Boolean> comparison) {
        int minSize = Math.min(v1.array.length, v2.array.length);

        for (int i = 0; i < minSize; i++) {
            long a = v1.array[i] & 0xFFFFFFFFL;
            long b = v2.array[i] & 0xFFFFFFFFL;
            if (!comparison.apply(a, b)) return false;
        }

        // Якщо розміри різні
        if (v1.array.length > v2.array.length) {
            for (int i = minSize; i < v1.array.length; i++) {
                long a = v1.array[i] & 0xFFFFFFFFL;
                if (!comparison.apply(a, 0L)) return false;
            }
        } else if (v2.array.length > v1.array.length) {
            return false;
        }

        return true;
    }

    // ==================== АРИФМЕТИЧНІ ОПЕРАТОРИ ====================

    // + для двох векторів
    public VectorUInt add(VectorUInt other) {
        return applyBinary(this, other, (a, b) -> a + b);
    }

    // + для вектора і скаляра
    public VectorUInt add(int scalar) {
        VectorUInt result = new VectorUInt(array.length);
        long s = scalar & 0xFFFFFFFFL;
        for (int i = 0; i < array.length; i++) {
            long val = (array[i] & 0xFFFFFFFFL) + s;
            result.array[i] = (int) (val > 0xFFFFFFFFL ? 0xFFFFFFFFL : val);
        }
        return result;
    }

    // - для двох векторів
    public VectorUInt subtract(VectorUInt other) {
        return applyBinary(this, other, (a, b) -> a >= b ? a - b : 0);
    }

    // - для вектора і скаляра
    public VectorUInt subtract(int scalar) {
        VectorUInt result = new VectorUInt(array.length);
        long s = scalar & 0xFFFFFFFFL;
        for (int i = 0; i < array.length; i++) {
            long val = (array[i] & 0xFFFFFFFFL);
            result.array[i] = (int) (val >= s ? val - s : 0);
        }
        return result;
    }

    // * для двох векторів
    public VectorUInt multiply(VectorUInt other) {
        return applyBinary(this, other, (a, b) -> {
            long product = a * b;
            return product > 0xFFFFFFFFL ? 0xFFFFFFFFL : product;
        });
    }

    // * для вектора і скаляра
    public VectorUInt multiply(int scalar) {
        VectorUInt result = new VectorUInt(array.length);
        long s = Math.abs((long) scalar);
        for (int i = 0; i < array.length; i++) {
            long val = (array[i] & 0xFFFFFFFFL) * s;
            result.array[i] = (int) (val > 0xFFFFFFFFL ? 0xFFFFFFFFL : val);
        }
        return result;
    }

    // / для двох векторів
    public VectorUInt divide(VectorUInt other) {
        return applyBinary(this, other, (a, b) -> b != 0 ? a / b : 0);
    }

    // / для вектора і скаляра
    public VectorUInt divide(short scalar) {
        VectorUInt result = new VectorUInt(array.length);
        if (scalar == 0) {
            result.codeError = 3;
            return result;
        }
        long s = Math.abs((long) scalar);
        for (int i = 0; i < array.length; i++) {
            long val = (array[i] & 0xFFFFFFFFL);
            result.array[i] = (int) (val / s);
        }
        return result;
    }

    // % для двох векторів
    public VectorUInt modulo(VectorUInt other) {
        return applyBinary(this, other, (a, b) -> b != 0 ? a % b : 0);
    }

    // % для вектора і скаляра
    public VectorUInt modulo(short scalar) {
        VectorUInt result = new VectorUInt(array.length);
        if (scalar == 0) {
            result.codeError = 3;
            return result;
        }
        long s = Math.abs((long) scalar);
        for (int i = 0; i < array.length; i++) {
            long val = (array[i] & 0xFFFFFFFFL);
            result.array[i] = (int) (val % s);
        }
        return result;
    }

    // ==================== ПОБІТОВІ ОПЕРАТОРИ ====================

    // | (побітове АБО) для двох векторів
    public VectorUInt bitwiseOr(VectorUInt other) {
        return applyBinary(this, other, (a, b) -> a | b);
    }

    // | для вектора і скаляра
    public VectorUInt bitwiseOr(int scalar) {
        VectorUInt result = new VectorUInt(array.length);
        for (int i = 0; i < array.length; i++) {
            result.array[i] = array[i] | scalar;
        }
        return result;
    }

    // ^ (побітове XOR) для двох векторів
    public VectorUInt bitwiseXor(VectorUInt other) {
        return applyBinary(this, other, (a, b) -> a ^ b);
    }

    // ^ для вектора і скаляра
    public VectorUInt bitwiseXor(int scalar) {
        VectorUInt result = new VectorUInt(array.length);
        for (int i = 0; i < array.length; i++) {
            result.array[i] = array[i] ^ scalar;
        }
        return result;
    }

    // & (побітове І) для двох векторів
    public VectorUInt bitwiseAnd(VectorUInt other) {
        int maxSize = Math.max(array.length, other.array.length);
        int minSize = Math.min(array.length, other.array.length);

        VectorUInt result = new VectorUInt(maxSize);
        for (int i = 0; i < minSize; i++) {
            result.array[i] = array[i] & other.array[i];
        }
        // Решта залишається 0 (за замовчуванням)
        return result;
    }

    // & для вектора і скаляра
    public VectorUInt bitwiseAnd(int scalar) {
        VectorUInt result = new VectorUInt(array.length);
        for (int i = 0; i < array.length; i++) {
            result.array[i] = array[i] & scalar;
        }
        return result;
    }

    // >> (зсув вправо)
    public VectorUInt rightShift(int scalar) {
        VectorUInt result = new VectorUInt(array.length);
        int shift = scalar % 32;
        for (int i = 0; i < array.length; i++) {
            result.array[i] = array[i] >>> shift; // беззнаковий зсув
        }
        return result;
    }

    // >> з вектором
    public VectorUInt rightShift(VectorUInt other) {
        return applyBinary(this, other, (a, b) -> a >>> (b % 32));
    }

    // << (зсув вліво)
    public VectorUInt leftShift(int scalar) {
        VectorUInt result = new VectorUInt(array.length);
        int shift = scalar % 32;
        for (int i = 0; i < array.length; i++) {
            result.array[i] = array[i] << shift;
        }
        return result;
    }

    // << з вектором
    public VectorUInt leftShift(VectorUInt other) {
        return applyBinary(this, other, (a, b) -> a << (b % 32));
    }

    // ==================== ОПЕРАТОРИ ПОРІВНЯННЯ ====================

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof VectorUInt)) return false;
        VectorUInt other = (VectorUInt) obj;
        return compareVectors(this, other, (a, b) -> a == b);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }

    public boolean notEquals(VectorUInt other) {
        return !equals(other);
    }

    public boolean greaterThan(VectorUInt other) {
        return compareVectors(this, other, (a, b) -> a > b);
    }

    public boolean greaterOrEqual(VectorUInt other) {
        return compareVectors(this, other, (a, b) -> a >= b);
    }

    public boolean lessThan(VectorUInt other) {
        return compareVectors(this, other, (a, b) -> a < b);
    }

    public boolean lessOrEqual(VectorUInt other) {
        return compareVectors(this, other, (a, b) -> a <= b);
    }

    // ==================== ДОДАТКОВІ МЕТОДИ ====================

    // Пошук максимального елемента
    public long max() {
        if (array.length == 0) return 0;
        long max = array[0] & 0xFFFFFFFFL;
        for (int i = 1; i < array.length; i++) {
            long val = array[i] & 0xFFFFFFFFL;
            if (val > max) max = val;
        }
        return max;
    }

    // Пошук мінімального елемента
    public long min() {
        if (array.length == 0) return 0;
        long min = array[0] & 0xFFFFFFFFL;
        for (int i = 1; i < array.length; i++) {
            long val = array[i] & 0xFFFFFFFFL;
            if (val < min) min = val;
        }
        return min;
    }

    // Сума всіх елементів
    public long sum() {
        long sum = 0;
        for (int val : array) {
            sum += val & 0xFFFFFFFFL;
        }
        return sum;
    }

    // Середнє арифметичне
    public double average() {
        if (array.length == 0) return 0;
        return (double) sum() / array.length;
    }

    // Сортування (новий вектор)
    public VectorUInt sorted() {
        VectorUInt result = new VectorUInt(this);
        Arrays.sort(result.array);
        return result;
    }

    // Реверс (новий вектор)
    public VectorUInt reversed() {
        VectorUInt result = new VectorUInt(array.length);
        for (int i = 0; i < array.length; i++) {
            result.array[i] = array[array.length - 1 - i];
        }
        return result;
    }

    // Перевірка чи всі елементи рівні 0
    public boolean isAllZero() {
        for (int val : array) {
            if (val != 0) return false;
        }
        return true;
    }

    // Підрахунок кількості ненульових елементів
    public int countNonZero() {
        int count = 0;
        for (int val : array) {
            if (val != 0) count++;
        }
        return count;
    }
}