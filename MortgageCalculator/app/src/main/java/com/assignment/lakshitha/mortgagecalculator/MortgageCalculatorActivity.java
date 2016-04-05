package com.assignment.lakshitha.mortgagecalculator;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MortgageCalculatorActivity extends AppCompatActivity {

    private EditText amountBorrowedEditText;
    private TextView interestRateSekBarTextView;
    private TextView finalAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final TextView interestTextView =(TextView) findViewById(R.id.interestRateSeekBarTextField);
        final SeekBar sk=(SeekBar) findViewById(R.id.interestRateSeekBar);

        double value = ((double)sk.getProgress() / 10.0);
        String progressValue = String.valueOf(value+ "%");
        interestTextView.setText(progressValue);

        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                double value = ((double) progress / 10.0);
                String progressValue = String.valueOf(value + "%");
                interestTextView.setText(progressValue);
            }


        });

        amountBorrowedEditText = (EditText)findViewById(R.id.amountBorrowedTextField);
        BigDecimal parsed = new BigDecimal("0").setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        String formatted = NumberFormat.getCurrencyInstance(Locale.US).format(parsed);
        amountBorrowedEditText.setText(formatted);
        amountBorrowedEditText.setSelection(amountBorrowedEditText.getText().length());
        amountBorrowedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                EditText editText = amountBorrowedEditText;
                if (editText == null) return;
                String s = editable.toString();
                editText.removeTextChangedListener(this);
                String cleanString = s.toString().replaceAll("[$,.]", "");
                BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
                String formatted = NumberFormat.getCurrencyInstance(Locale.US).format(parsed);
                editText.setText(formatted);
                editText.setSelection(formatted.length());
                editText.addTextChangedListener(this);

            }
        });
        interestRateSekBarTextView = (TextView) findViewById(R.id.interestRateSeekBarTextField);

        finalAmountTextView = (TextView)findViewById(R.id.finalAmountText);

        Button calculateButton = (Button)findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(calculateButtonListener);
    }

    public View.OnClickListener calculateButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            calculate();
        }
    };

    private void calculate(){

        String amountString = amountBorrowedEditText.getText().toString().replaceAll("[$,]", "");

        if(amountString.matches(""))
            showAlertDialog();
        else {
            double amountBorrowed = Double.parseDouble(amountString);
            if(amountBorrowed == 0)
                showAlertDialog();
            else
            {
                double annualInterestRate = Double.parseDouble(interestRateSekBarTextView.getText().toString().replace('%', ' '));
                double monthlyInterestRate = annualInterestRate / 1200;

                RadioGroup rg = (RadioGroup) findViewById(R.id.loanTermRadio);

                int loanTerminYears = Integer.parseInt(((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString().replaceAll("[^0-9]", ""));
                int loanTerminMonths = loanTerminYears * 12;

                double taxesAndInsurance = 0.0;
                CheckBox cb = (CheckBox) findViewById(R.id.taxesInsuranceCheckBox);
                if (cb.isChecked())
                    taxesAndInsurance = (0.1 * amountBorrowed) / 100;

                double finalAmount = 0.0;
                if (monthlyInterestRate != 0) {
                    finalAmount = (amountBorrowed * (monthlyInterestRate / (1 - (Math.pow(1 + monthlyInterestRate, -loanTerminMonths))))) + taxesAndInsurance;
                } else {
                    finalAmount = (amountBorrowed / loanTerminMonths) + taxesAndInsurance;
                }

                NumberFormat moneyFormat =
                        NumberFormat.getCurrencyInstance(Locale.US);

                finalAmountTextView.setText(moneyFormat.format(finalAmount));
            }
        }


    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MortgageCalculatorActivity.this);
        builder.setTitle("Input Error");
        builder.setPositiveButton("OK", null);
        builder.setMessage("Please enter a valid Amount");
        AlertDialog errorDialog = builder.create();
        errorDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
