/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.codehaus.groovy.runtime.typehandling;

import java.math.BigDecimal;
import java.math.BigInteger;

import static java.math.MathContext.DECIMAL64;

/**
 * Created by tomto on 5/6/2017.
 */
public class FastMath extends NumberMath {

    public static final FastMath INSTANCE = new FastMath();
    private FastMath() {}

    @Override
    protected Number absImpl(Number number) {

        if (number==null) return 0;

        if (number instanceof Double) {
            return Math.abs((Double)number);
        } else if (number instanceof Float) {
            return Math.abs((Float)number);
        } else if (number instanceof Long) {
            return Math.abs((Long)number);
        } else if (number instanceof Integer || number instanceof Short || number instanceof Byte) {
            return Math.abs(number.intValue());
        } else if (number instanceof BigDecimal) {
            return ((BigDecimal)number).abs();
        } else if (number instanceof BigInteger) {
            return ((BigInteger)number).abs();
        }
        throw new RuntimeException("Unsupported number type:" + number.getClass());
    }

    @Override
    public Number addImpl(Number left, Number right) {

        if (left==null) return right;
        if (right==null) return left;

        if (left instanceof BigDecimal || right instanceof BigDecimal) {
            return toBigDecimalLocal(left).add(toBigDecimalLocal(right));
        } else if (left instanceof Double || right instanceof Double) {
            return left.doubleValue() + right.doubleValue();
        } else if (left instanceof Float || right instanceof Float) {
            return left.floatValue() + right.doubleValue();
        } else if (left instanceof BigInteger || right instanceof BigInteger) {
            return toBigIntegerLocal(left).add(toBigIntegerLocal(right));
        } else if (left instanceof Long || right instanceof Long) {
            if (right instanceof Long || right instanceof Integer || right instanceof Short || right instanceof Byte) {
                return left.longValue() + right.longValue();
            } else {
                return left.doubleValue() + right.doubleValue();
            }
        } else if (left instanceof Integer || left instanceof Short || left instanceof Byte) {
            if (right instanceof Integer || right instanceof Short || right instanceof Byte) {
                return left.intValue() + right.intValue();
            } else {
                return left.doubleValue() + right.doubleValue();
            }
        }
        throw new RuntimeException("Unsupported number type:" + left.getClass() + " or " + right.getClass());
    }

    @Override
    public Number subtractImpl(Number left, Number right) {
        if (left==null) return right;
        if (right==null) return left;

        if (left instanceof BigDecimal || right instanceof BigDecimal) {
            return toBigDecimalLocal(left).subtract(toBigDecimalLocal(right));
        } else if (left instanceof Double || right instanceof Double) {
            return left.doubleValue() - right.doubleValue();
        } else if (left instanceof Float || right instanceof Float) {
            return left.floatValue() - right.doubleValue();
        } else if (left instanceof BigInteger || right instanceof BigInteger) {
            return toBigIntegerLocal(left).subtract(toBigIntegerLocal(right));
        } else if (left instanceof Long || right instanceof Long) {
            if (right instanceof Long || right instanceof Integer || right instanceof Short) {
                return left.longValue() - right.longValue();
            } else {
                return left.doubleValue() - right.doubleValue();
            }
        } else if (left instanceof Integer || left instanceof Short || left instanceof Byte) {
            if (right instanceof Integer || right instanceof Short || right instanceof Byte) {
                return left.intValue() - right.intValue();
            } else {
                return left.doubleValue() - right.doubleValue();
            }
        }
        throw new RuntimeException("Unsupported number type:" + left.getClass() + " or " + right.getClass());
    }

    @Override
    public Number multiplyImpl(Number left, Number right) {
        if (left==null || right==null) return 0;

        if (left instanceof BigDecimal || right instanceof BigDecimal) {
            return toBigDecimalLocal(left).multiply(toBigDecimalLocal(right));
        } else if (left instanceof Double || right instanceof Double) {
            return left.doubleValue() * right.doubleValue();
        } else if (left instanceof Float || right instanceof Float) {
            return left.floatValue() * right.doubleValue();
        } else if (left instanceof BigInteger || right instanceof BigInteger) {
            return toBigIntegerLocal(left).multiply(toBigIntegerLocal(right));
        } else if (left instanceof Long || right instanceof Long) {
            if (right instanceof Long || right instanceof Integer || right instanceof Short) {
                return left.longValue() * right.longValue();
            } else {
                return left.doubleValue() * right.doubleValue();
            }
        } else if (left instanceof Integer || left instanceof Short || left instanceof Byte) {
            if (right instanceof Integer || right instanceof Short || right instanceof Byte) {
                return left.intValue() * right.intValue();
            } else {
                return left.doubleValue() * right.doubleValue();
            }
        }
        throw new RuntimeException("Unsupported number type:" + left.getClass() + " or " + right.getClass());
    }

    @Override
    public Number divideImpl(Number left, Number right) {

        if (left==null) return 0;
        if (right==null || right.doubleValue()==0.0d) {
            throw new ArithmeticException("Divide by zero");
        }

        if (left instanceof BigDecimal || right instanceof BigDecimal) {
            try {
                return toBigDecimalLocal(left).divide(toBigDecimalLocal(right), DECIMAL64);
            } catch (ArithmeticException var9) {
                return left.doubleValue() / right.doubleValue();
            }
        } else if (left instanceof Double || right instanceof Double) {
            return left.doubleValue() / right.doubleValue();
        } else if (left instanceof Float || right instanceof Float) {
            return left.doubleValue() / right.doubleValue();
        } else if (left instanceof BigInteger || right instanceof BigInteger) {
            try {
                return toBigDecimalLocal(left).divide(toBigDecimalLocal(right), DECIMAL64);
            } catch (ArithmeticException var9) {
                return left.doubleValue() / right.doubleValue();
            }
        } else if (left instanceof Long) {
            return left.doubleValue() / right.doubleValue();
        } else if (left instanceof Integer || left instanceof Short || left instanceof Byte) {
            return left.doubleValue() / right.doubleValue();
        }
        throw new RuntimeException("Unsupported number type:" + left.getClass() + " or " + right.getClass());
    }

    @Override
    public int compareToImpl(Number left, Number right) {
        if (left==null) left = 0;
        if (right==null) right = 0;

        if (left instanceof BigDecimal || right instanceof BigDecimal) {
            return toBigDecimalLocal(left).compareTo(toBigDecimalLocal(right));
        } else if (left instanceof Double || right instanceof Double) {
            return Double.compare(left.doubleValue(), right.doubleValue());
        } else if (left instanceof Float || right instanceof Float) {
            return Float.compare(left.floatValue(), right.floatValue());
        } else if (left instanceof BigInteger || right instanceof BigInteger) {
            return toBigIntegerLocal(left).compareTo(toBigIntegerLocal(right));
        } else if (left instanceof Long || left instanceof Integer || left instanceof Short || left instanceof Byte) {
            if (right instanceof Long || right instanceof Integer || right instanceof Short || right instanceof Byte) {
                return (left.longValue() < right.longValue()) ? -1 : ((left.longValue() == right.longValue()) ? 0 : 1);
            } else {
                return (left.doubleValue() < right.doubleValue()) ? -1 : ((left.doubleValue() == right.doubleValue()) ? 0 : 1);
            }
        }
        throw new RuntimeException("Unsupported number type:" + left.getClass() + " or " + right.getClass());
    }

    @Override
    protected Number unaryMinusImpl(Number number) {

        if (number==null) return null;

        if (number instanceof Double) {
            return -((Double)number);
        } else if (number instanceof Float) {
            return -((Float)number);
        } else if (number instanceof Long) {
            return -((Long)number);
        } else if (number instanceof Integer) {
            return -((Integer)number);
        } else if (number instanceof Short) {
            return -((Short)number);
        } else if (number instanceof Byte) {
            return -((Byte)number);
        } else if (number instanceof BigDecimal) {
            return ((BigDecimal)number).negate();
        } else if (number instanceof BigInteger) {
            return ((BigInteger)number).negate();
        }
        throw new RuntimeException("Unsupported number type:" + number.getClass());
    }

    @Override
    protected Number unaryPlusImpl(Number number) {
        return number;
    }

    protected Number modImpl(Number left, Number right) {
        if (left==null || right==null) return 0;

        if (left instanceof BigDecimal || right instanceof BigDecimal) {
            return new BigDecimal(left.doubleValue() % right.doubleValue(), DECIMAL64);
        } else if (left instanceof Double || right instanceof Double) {
            return left.doubleValue() % right.doubleValue();
        } else if (left instanceof Float || right instanceof Float) {
            return left.floatValue() % right.doubleValue();
        } else if (left instanceof BigInteger || right instanceof BigInteger) {
            return new BigDecimal(left.doubleValue() % right.doubleValue(), DECIMAL64);
        } else if (left instanceof Long || right instanceof Long) {
            if (right instanceof Long || right instanceof Integer || right instanceof Short || right instanceof Byte) {
                return left.longValue() % right.longValue();
            } else {
                return left.doubleValue() % right.doubleValue();
            }
        } else if (left instanceof Integer || left instanceof Short || left instanceof Byte) {
            if (right instanceof Integer || right instanceof Short) {
                return left.intValue() % right.intValue();
            } else {
                return left.doubleValue() % right.doubleValue();
            }
        }
        throw new RuntimeException("Unsupported number type:" + left.getClass() + " or " + right.getClass());
    }

    public BigDecimal toBigDecimalLocal(Number n) {
        return (n instanceof BigDecimal ? (BigDecimal) n : new BigDecimal(n.toString(), DECIMAL64));
    }

    public BigInteger toBigIntegerLocal(Number n) {
        return (n instanceof BigInteger ? (BigInteger) n : new BigInteger(n.toString()));
    }

}
