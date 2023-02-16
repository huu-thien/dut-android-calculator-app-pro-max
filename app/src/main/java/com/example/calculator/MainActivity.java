package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('x')) x *= parseFactor(); // multiplication
                    else if (eat(':')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
    public static String fmt(double d) {
        if (d == (int) d)
            return String.format("%d", (int) d);
        else
            return String.format("%s", d);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btn0.setOnClickListener(e -> {
            binding.expression.append("0");
        });
        binding.btn1.setOnClickListener(e -> {
            binding.expression.append("1");
        });
        binding.btn2.setOnClickListener(e -> {
            binding.expression.append("2");
        });
        binding.btn3.setOnClickListener(e -> {
            binding.expression.append("3");
        });
        binding.btn4.setOnClickListener(e -> {
            binding.expression.append("4");
        });
        binding.btn5.setOnClickListener(e -> {
            binding.expression.append("5");
        });
        binding.btn6.setOnClickListener(e -> {
            binding.expression.append("6");
        });
        binding.btn7.setOnClickListener(e -> {
            binding.expression.append("7");
        });
        binding.btn8.setOnClickListener(e -> {
            binding.expression.append("8");
        });
        binding.btn9.setOnClickListener(e -> {
            binding.expression.append("9");
        });
        binding.btnPlus.setOnClickListener(e -> {
            binding.expression.append("+");
        });
        binding.btnMinus.setOnClickListener(e -> {
            binding.expression.append("-");
        });
        binding.btnMultiply.setOnClickListener(e -> {
            binding.expression.append("x");
        });
        binding.btnDivide.setOnClickListener(e -> {
            binding.expression.append(":");
        });
        binding.btnDot.setOnClickListener(e -> {
            binding.expression.append(".");
        });
        binding.btnParenthesesLeft.setOnClickListener(e -> {
            binding.expression.append("(");
        });
        binding.btnParenthesesRight.setOnClickListener(e -> {
            binding.expression.append(")");
        });
        binding.btnEqual.setOnClickListener(e -> {
            String input = binding.expression.getText().toString();
            if(input.length() != 0) {
                binding.expression.setText("");
                binding.expression.append(fmt(eval(input)));
                binding.expressionHistory.setText("");
                binding.expressionHistory.append(input);
                binding.expressionHistory.append(" = " + fmt(eval(input)));
            }
            else {
                binding.expression.setText("0");
                binding.expressionHistory.setText("0");
            }
        });
        binding.btnDelete.setOnClickListener(e -> {
            String currentResult = binding.expression.getText().toString();
            if (currentResult.length() != 0) {
                currentResult = currentResult.substring(0, currentResult.length() - 1);
                binding.expression.setText(currentResult);
            }
        });
        binding.btnClear.setOnClickListener(e -> {
            binding.expression.setText("");
            binding.expressionHistory.setText("");
        });
    }
}