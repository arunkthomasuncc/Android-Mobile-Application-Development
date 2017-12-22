package com.example.arun.calculator;
/*
 Assignment: Homework 1
 File Name : Calculator
 Members   : Arun Kunnumpuram Thomas(801027386), Ron Abraham Cherian(801028678)
*/
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {


    static boolean isClearScreenEnabled = false;
    static boolean isResultError = false;
    static String operation = null;
    static Double operand1 = null;
    static Double operand2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button one = (Button) findViewById(R.id.button1);
        final Button two = (Button) findViewById(R.id.button2);
        final Button three = (Button) findViewById(R.id.button3);
        final Button four = (Button) findViewById(R.id.button4);
        final Button five = (Button) findViewById(R.id.button5);
        final Button six = (Button) findViewById(R.id.button6);
        final Button seven = (Button) findViewById(R.id.button7);
        final Button eight = (Button) findViewById(R.id.button8);
        final Button nine = (Button) findViewById(R.id.button9);
        final Button zero = (Button) findViewById(R.id.button0);
        final Button plus = (Button) findViewById(R.id.buttonPlus);
        final Button minus = (Button) findViewById(R.id.buttonMinus);
        final Button multiplication = (Button) findViewById(R.id.buttonMultiplication);
        final Button division = (Button) findViewById(R.id.buttonDivision);
        final Button AC = (Button) findViewById(R.id.buttonAC);
        final Button dot = (Button) findViewById(R.id.buttondot);
        final Button equal = (Button) findViewById(R.id.buttonEqual);
        final TextView result = (TextView) findViewById(R.id.textViewResult);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isClearScreenEnabled || isResultError) {
                    result.setText(1 + "");
                    isClearScreenEnabled = false;
                    isResultError = false;
                } else {
/*                    if (((result.getText().toString().length() == 14) && (!result.getText().toString().contains("."))) || ((result.getText().toString().length() == 15) && (result.getText().toString().contains(".")))) {

                    }
                    else
                    {*/
                    String currentNumber = result.getText().toString().trim();
                    result.setText(currentNumber + 1);
                }
            //}
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isClearScreenEnabled || isResultError) {
                    result.setText(2 + "");
                    isClearScreenEnabled = false;
                    isResultError = false;
                } else {
                    /*if (((result.getText().toString().length() == 14) && (!result.getText().toString().contains("."))) || ((result.getText().toString().length() == 15) && (result.getText().toString().contains(".")))) {

                    } else {*/
                        String currentNumber = result.getText().toString().trim();
                        result.setText(currentNumber + 2);
                    //}
                }
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isClearScreenEnabled || isResultError) {
                    result.setText(3 + "");
                    isClearScreenEnabled = false;
                    isResultError = false;
                } else {
                    /*if (((result.getText().toString().length() == 14) && (!result.getText().toString().contains("."))) || ((result.getText().toString().length() == 15) && (result.getText().toString().contains(".")))) {

                    } else {*/
                        String currentNumber = result.getText().toString().trim();
                        result.setText(currentNumber + 3);
                    //}
                }
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isClearScreenEnabled || isResultError) {
                    result.setText(4 + "");
                    isClearScreenEnabled = false;
                    isResultError = false;
                } else {
                    /*if (((result.getText().toString().length() == 14) && (!result.getText().toString().contains("."))) || ((result.getText().toString().length() == 15) && (result.getText().toString().contains(".")))) {

                    } else {*/
                        String currentNumber = result.getText().toString().trim();
                        result.setText(currentNumber + 4);
                    //}
                }
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isClearScreenEnabled || isResultError) {
                    result.setText(5 + "");
                    isClearScreenEnabled = false;
                    isResultError = false;
                } else {
                    /*if (((result.getText().toString().length() == 14) && (!result.getText().toString().contains("."))) || ((result.getText().toString().length() == 15) && (result.getText().toString().contains(".")))) {

                    } else {*/
                        String currentNumber = result.getText().toString().trim();
                        result.setText(currentNumber + 5);
                   // }
                }
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isClearScreenEnabled || isResultError) {
                    result.setText(6 + "");
                    isClearScreenEnabled = false;
                    isResultError = false;
                } else {
                    /*if (((result.getText().toString().length() == 14) && (!result.getText().toString().contains("."))) || ((result.getText().toString().length() == 15) && (result.getText().toString().contains(".")))) {

                    } else {*/
                        String currentNumber = result.getText().toString().trim();
                        result.setText(currentNumber + 6);
                    //}
                }
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isClearScreenEnabled || isResultError) {
                    result.setText(7 + "");
                    isClearScreenEnabled = false;
                    isResultError = false;
                } else {
                   /* if (((result.getText().toString().length() == 14) && (!result.getText().toString().contains("."))) || ((result.getText().toString().length() == 15) && (result.getText().toString().contains(".")))) {

                    } else {*/
                        String currentNumber = result.getText().toString().trim();
                        result.setText(currentNumber + 7);
                  //  }
                }
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isClearScreenEnabled || isResultError) {
                    result.setText(8 + "");
                    isClearScreenEnabled = false;
                    isResultError = false;
                } else {
                    /*if (((result.getText().toString().length() == 14) && (!result.getText().toString().contains("."))) || ((result.getText().toString().length() == 15) && (result.getText().toString().contains(".")))) {

                    } else {*/
                        String currentNumber = result.getText().toString().trim();
                        result.setText(currentNumber + 8);
                    //}
                }
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isClearScreenEnabled || isResultError) {
                    result.setText(9 + "");
                    isClearScreenEnabled = false;
                    isResultError = false;
                } else {
                    /*if (((result.getText().toString().length() == 14) && (!result.getText().toString().contains("."))) || ((result.getText().toString().length() == 15) && (result.getText().toString().contains(".")))) {

                    } else {*/
                        String currentNumber = result.getText().toString().trim();
                        result.setText(currentNumber + 9);
                    //}
                }
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isClearScreenEnabled || isResultError) {
                    result.setText(0 + "");
                    isClearScreenEnabled = false;
                    isResultError = false;
                } else {
                   /* if (((result.getText().toString().length() == 14) && (!result.getText().toString().contains("."))) || ((result.getText().toString().length() == 15) && (result.getText().toString().contains(".")))) {

                    } else {*/
                        String currentNumber = result.getText().toString().trim();
                        result.setText(currentNumber + 0);
                   // }
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isResultError) {
                    if (operation != null && isClearScreenEnabled == false) {
                        operand2 = Double.parseDouble(result.getText().toString().trim());
                        Double resultToDisplayDouble = compute();
                        if (null == resultToDisplayDouble || Double.isInfinite(resultToDisplayDouble)) {
                            if (null == resultToDisplayDouble) {
                                result.setText("ERROR");
                            } else {
                                result.setText(resultToDisplayDouble + "");
                            }
                            isResultError = true;

                        } else {

                            String output = precisionSetter(resultToDisplayDouble);
                            if (output == null) {
                                result.setText("ERROR");
                                operand1 = 0.0;
                                operand2 = 0.0;
                                operation = null;
                                isClearScreenEnabled = true;
                                isResultError = true;
                            } else {
                                result.setText(output + "");
                                operation = "ADDITION";
                                isClearScreenEnabled = true;
                            }


                        }
                    } else {
                        operand1 = Double.parseDouble(result.getText().toString());
                        operation = "ADDITION";
                        isClearScreenEnabled = true;
                    }
                    Log.d("Calculator", "Operation:" + operation);
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (!isResultError ) {
                    if (operation != null && isClearScreenEnabled == false) {
                        operand2 = Double.parseDouble(result.getText().toString().trim());

                        Double resultToDisplayDouble = compute();
                        if (null == resultToDisplayDouble || Double.isInfinite(resultToDisplayDouble)) {
                            if (null == resultToDisplayDouble) {
                                result.setText("ERROR");
                            } else {
                                result.setText(resultToDisplayDouble + "");
                            }
                            isResultError = true;

                        } else {

                            String output = precisionSetter(resultToDisplayDouble);
                            if (output == null) {
                                result.setText("ERROR");
                                operand1 = 0.0;
                                operand2 = 0.0;
                                operation = null;
                                isClearScreenEnabled = true;
                                isResultError = true;
                            } else {
                                result.setText(output + "");
                                operation = "SUBTRACTION";
                                isClearScreenEnabled = true;
                            }


                        }
                    } else {
                        operand1 = Double.parseDouble(result.getText().toString());
                        operation = "SUBTRACTION";
                        isClearScreenEnabled = true;
                    }
                    Log.d("Calculator", "Operation:" + operation);
                }
            }
        });
        division.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (!isResultError) {
                    if (operation != null && isClearScreenEnabled == false) {
                        operand2 = Double.parseDouble(result.getText().toString().trim());
                        Double resultToDisplayDouble = compute();
                        if (null == resultToDisplayDouble || Double.isInfinite(resultToDisplayDouble)) {
                            if (null == resultToDisplayDouble) {
                                result.setText("ERROR");
                            } else {
                                result.setText(resultToDisplayDouble + "");
                            }
                            isResultError = true;

                        } else {

                            String output = precisionSetter(resultToDisplayDouble);
                            if (output == null) {
                                result.setText("ERROR");
                                operand1 = 0.0;
                                operand2 = 0.0;
                                operation = null;
                                isClearScreenEnabled = true;
                                isResultError = true;
                            } else {
                                result.setText(output + "");
                                operation = "DIVISION";
                                isClearScreenEnabled = true;
                            }


                        }
                    } else {
                        operand1 = Double.parseDouble(result.getText().toString());
                        operation = "DIVISION";
                        isClearScreenEnabled = true;
                    }
                    Log.d("Calculator", "Operation:" + operation);
                }
            }
        });
        multiplication.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (!isResultError) {
                    if (operation != null && isClearScreenEnabled == false) {
                        operand2 = Double.parseDouble(result.getText().toString().trim());

                        Double resultToDisplayDouble = compute();
                        if (null == resultToDisplayDouble || Double.isInfinite(resultToDisplayDouble)) {
                            if (null == resultToDisplayDouble) {
                                result.setText("ERROR");
                            } else {
                                result.setText(resultToDisplayDouble + "");
                            }
                            isResultError = true;

                        } else {

                            String output = precisionSetter(resultToDisplayDouble);
                            if (output == null) {
                                result.setText("ERROR");
                                operand1 = 0.0;
                                operand2 = 0.0;
                                operation = null;
                                isClearScreenEnabled = true;
                                isResultError = true;
                            } else {
                                result.setText(output + "");
                                operation = "MULTIPLICATION";
                                isClearScreenEnabled = true;
                            }


                        }
                    } else {
                        operand1 = Double.parseDouble(result.getText().toString());
                        operation = "MULTIPLICATION";
                        isClearScreenEnabled = true;
                    }
                    Log.d("Calculator", "Operation:" + operation);
                }
            }
        });


        dot.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                if (isResultError || isClearScreenEnabled) {
                    isResultError = false;
                    isClearScreenEnabled = false;
                    result.setText(".");

                } else {
                   /* if (result.getText().toString().length() == 14) {

                    } else {*/
                        if (!result.getText().toString().contains(".")) {

                            result.setText(result.getText().toString() + ".");

                        }
                    }

               // }
            }
        });
        equal.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if(isClearScreenEnabled)
                    return;

                operand2 = Double.parseDouble(result.getText().toString().trim());
                if (operation != null) {
                    Double op = compute();

                    if (null == op || Double.isInfinite(op)) {
                        if (null == op) {
                            result.setText("ERROR");
                        } else {
                            result.setText(op + "");
                        }
                        isResultError = true;

                    } else {
                        String output = precisionSetter(op);

                        if (output == null) {
                            isResultError = true;
                            result.setText("ERROR");

                        } else {

                            result.setText(output + "");
                            isClearScreenEnabled = true;
                            operation = null;


                        }

                    }

                }
            }
        });
        AC.setOnClickListener(new View.OnClickListener()

                              {
                                  @Override
                                  public void onClick(View v) {
                                      result.setText(0 + "");
                                      operand1 = 0.0;
                                      operand2 = 0.0;
                                      operation = null;
                                      isClearScreenEnabled = true;
                                      isResultError = false;
                                  }
                              }
        );
    }

    public Double compute() {
        Double computedResult = null;
        if (operation == "ADDITION") {
            computedResult = operand1 + operand2;
            operand1 = computedResult;
        } else if (operation == "SUBTRACTION") {
            computedResult = (operand1) - (operand2);
            operand1 = computedResult;
        } else if (operation == "DIVISION") {

            computedResult = (operand1) / (operand2);
            if (Double.isNaN(computedResult)) {
                return null;
            }
            operand1 = computedResult;

        } else {
            computedResult = (operand1) * (operand2);
            operand1 = computedResult;
        }
        operation = null;
        return computedResult;
    }


    public String precisionSetter(Double d)

    {
        String output = null;
        BigDecimal bd = new BigDecimal(d.toString());
        String value = bd.stripTrailingZeros().toPlainString();
        if (value.equals("0.0")) {
            return "0";
        }
        if (value.length() >= 15 && !value.contains(".")) {
            return null;
        } else if (value.length() < 15 && !value.contains(".")) {
            return value;
        } else if (value.length() < 15 && value.contains(".")) {
            return value;

        } else if (value.length() == 15 && value.contains(".")) {

            if (value.charAt(14) == '.') {
                return value.substring(0, value.indexOf("."));
            }
            return value;
        } else if (value.length() > 15 && value.contains("."))

        {
            int integerLenValue = value.substring(0, value.indexOf(".")).length();

            int decimalLenValue = 0;

            decimalLenValue = 15 - 1 - integerLenValue;
            if (decimalLenValue == 0) {
                return value.substring(0, value.indexOf("."));
            } else if (decimalLenValue < 0) {
                return null;
            } else {
                BigDecimal newBD = bd.setScale(decimalLenValue, RoundingMode.HALF_UP);
                return (newBD.stripTrailingZeros().toPlainString());
            }
        } else
            return null;
    }
}
