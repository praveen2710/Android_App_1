package praveenbanthia.androidtutorialwesley;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "praveenbanthia.androidtutorialwesley.MESSAGE";
    private final static String TOTAL_BILL = "TOTAL_BILL";
    private final static String TIP_PERCENT = "TIP_PERCENT";
    private final static String FINAL_BILL = "FINAL_BILL";

    private int checkListValues[] = new int[12];

    private double bill_before_tip;
    private double tip_percent;
    private double final_bill;

    EditText billBeforeTipET;
    EditText tipPercentET;
    EditText finalBillET;

    CheckBox friendlyCB;
    CheckBox specialCB;
    CheckBox opnionCB;

    RadioGroup avaliabilityRG;
    RadioButton goobRB;
    RadioButton okRB;
    RadioButton badRB;

    Spinner problemSpinner;

    SeekBar tipChangeSeeker;

    Button startChronoB;
    Button pauseChromoB;
    Button resetChronoB;

    Chronometer waitTimeChronometer;

    long secondswaited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            bill_before_tip  = 0.0;
            tip_percent = 0.15;
            final_bill  = 0.0;
        }else{
            bill_before_tip =  savedInstanceState.getDouble(TOTAL_BILL);
            tip_percent =  savedInstanceState.getDouble(TIP_PERCENT);
            final_bill =  savedInstanceState.getDouble(FINAL_BILL);
        }

        billBeforeTipET = (EditText) findViewById(R.id.billEditText);
        tipPercentET    = (EditText) findViewById(R.id.tipEditText);
        finalBillET     = (EditText) findViewById(R.id.finalBillEditText);
        tipChangeSeeker = (SeekBar) findViewById(R.id.tipChangeSeekBar);

        tipChangeSeeker.setOnSeekBarChangeListener(tipChangeSeekListener);

        billBeforeTipET.addTextChangedListener(billBeforeTipListener);

        friendlyCB = (CheckBox)findViewById(R.id.friendlyCheckBox);
        opnionCB   = (CheckBox)findViewById(R.id.opnionCheckBox);
        specialCB  = (CheckBox)findViewById(R.id.specialsCheckBox);

        setUpIntoCheckBoxes();

        avaliabilityRG = (RadioGroup) findViewById(R.id.avaliableRadioGroup);
        badRB          = (RadioButton) findViewById(R.id.badRButton);
        okRB           = (RadioButton) findViewById(R.id.okRButton);
        goobRB         = (RadioButton) findViewById(R.id.goodRButton);

        addChangeListenerToRadioGroup();

        problemSpinner = (Spinner) findViewById(R.id.problemsSpinner);

        addItemSelectedListenerToSpinner();

        startChronoB = (Button) findViewById(R.id.startChronoButton);
        pauseChromoB = (Button) findViewById(R.id.pauseChronoButton);
        resetChronoB = (Button) findViewById(R.id.resetChronoButton);

        setChronoButtonOnClickListener();

        waitTimeChronometer = (Chronometer) findViewById(R.id.waitChronometer);
    }


    private TextWatcher billBeforeTipListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                bill_before_tip = Double.parseDouble(s.toString());
            }catch(NumberFormatException e) {
                bill_before_tip = 0.0;
            }
            updateTipAndFinalBill();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void updateTipAndFinalBill(){

        tip_percent  = Double.parseDouble(tipPercentET.getText().toString());

        final_bill = bill_before_tip + (bill_before_tip * tip_percent);

        finalBillET.setText(String.format("%.02f",final_bill));

    }

    private OnSeekBarChangeListener tipChangeSeekListener = new OnSeekBarChangeListener(){


        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            tip_percent = (seekBar.getProgress()) * 0.01;

            tipPercentET.setText(String.format("%.02f",tip_percent));

            updateTipAndFinalBill();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

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
            Context context = getApplicationContext();
            CharSequence text = "Settings pressed";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context,text,duration);
            toast.show();
            return true;
        }else if (id == R.id.action_new){
            return true;
        }else if (id == R.id.action_search){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpIntoCheckBoxes(){

        Log.i("setUpIntoCheckBoxes","Came in here");

        friendlyCB.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkListValues[0] = friendlyCB.isChecked()?4:0;

                setTipFromWaitressChecklist();

                updateTipAndFinalBill();
            }
        });

        opnionCB.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkListValues[1] = opnionCB.isChecked()?2:0;

                setTipFromWaitressChecklist();

                updateTipAndFinalBill();
            }
        });

        specialCB.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkListValues[2] = opnionCB.isChecked() ? 1 : 0;

                setTipFromWaitressChecklist();

                updateTipAndFinalBill();
            }
        });
    }

    private void addChangeListenerToRadioGroup(){

      avaliabilityRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup group, int checkedId) {
              checkListValues[3] = badRB.isChecked() ? -2 : 0;
              checkListValues[4] = okRB.isChecked() ? 1 : 0;
              checkListValues[5] = goobRB.isChecked() ? 2 : 0;

              setTipFromWaitressChecklist();

              updateTipAndFinalBill();
          }
      });
    }

    private void addItemSelectedListenerToSpinner(){


        Log.i("addItemSelectedListenerToSpinner","Came in here");

       problemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               checkListValues[6] =
                       problemSpinner.getSelectedItem().toString().equals("Bad")?-2:0;
               checkListValues[7] =
                       problemSpinner.getSelectedItem().toString().equals("OK")?1:0;
               checkListValues[8] =
                      problemSpinner.getSelectedItem().toString().equals("Good")?3:0;

               setTipFromWaitressChecklist();

               updateTipAndFinalBill();

           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
    }

    private void setChronoButtonOnClickListener(){

        startChronoB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("StartChronoButton","came in here");
                int timeElaspedInMs = 0;
                String chronoText = waitTimeChronometer.getText().toString();
                String array[]=chronoText.split(":");
                if(array.length == 2){
                    timeElaspedInMs = (Integer.parseInt(array[0]) * 60 * 1000)
                                      +(Integer.parseInt(array[1]) * 1000);
                }else if(array.length == 3){
                    timeElaspedInMs = (Integer.parseInt(array[0]) * 60 * 60 * 100)
                                      +(Integer.parseInt(array[1]) * 60 * 1000)
                                      +(Integer.parseInt(array[2]) * 1000);
                }
                waitTimeChronometer.setBase(SystemClock.elapsedRealtime()-timeElaspedInMs);

                waitTimeChronometer.start();
                startChronoB.setEnabled(false);
            }
        });

        pauseChromoB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("PauseChronoButton","came in here");
                waitTimeChronometer.stop();
                Log.i("Time Now",waitTimeChronometer.getText().toString());
                startChronoB.setEnabled(true);
            }
        });

        resetChronoB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                waitTimeChronometer.stop();
                String chronoText = waitTimeChronometer.getText().toString();
                waitTimeChronometer.setBase(SystemClock.elapsedRealtime());
                String array[]=chronoText.split(":");

                double timeYouWaitedInMin = 0;
                Log.i("array length",""+array.length);
                if(array.length == 1){
                    timeYouWaitedInMin = Integer.parseInt(array[1])/60;
                }else if(array.length == 2){
                    timeYouWaitedInMin = Integer.parseInt(array[0]);
                }else if(array.length == 3){
                    timeYouWaitedInMin =Integer.parseInt(array[0])*60
                                        + Integer.parseInt(array[1]);
                }else{
                    timeYouWaitedInMin = Integer.parseInt(array[4]);
                }
                int roundWaitTime = (int)timeYouWaitedInMin;
                Log.i("minutes",""+roundWaitTime);
                updateTipBasedOnTimeWaited(roundWaitTime);

                setTipFromWaitressChecklist();
                updateTipAndFinalBill();

            }
        });
    }


    private void updateTipBasedOnTimeWaited(int waitTime){
         checkListValues[9] = (waitTime >10)?-2:2;
                 if(waitTime > 30){
                     System.out.print("Too Long");
                 }
    }

    private void setTipFromWaitressChecklist(){

        Log.i("setTipFromWaitressChecklist","Came in here");
        int checkListTotal = 0;

        for(int item:checkListValues) checkListTotal += item;

        tipPercentET.setText(String.format("%.02f", checkListTotal*0.01));
    }

    protected void onSaveInstanceState(Bundle outState){

        super.onSaveInstanceState(outState);

        outState.putDouble(FINAL_BILL, final_bill);
        outState.putDouble(TIP_PERCENT, tip_percent);
        outState.putDouble(TOTAL_BILL, bill_before_tip);

    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
