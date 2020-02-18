# PinEntryEditText

[![](https://jitpack.io/v/fahim44/PinEntryEditText.svg)](https://jitpack.io/#fahim44/PinEntryEditText)

[![86488963-195072508363870-5774829274615775232-n.png](https://i.postimg.cc/4Nm54bJy/86488963-195072508363870-5774829274615775232-n.png)](https://postimg.cc/bsfb3nSf)

This is a simple Varification pin entry editText library for Android. This library based on [Ali Muzaffar](https://medium.com/@ali.muzaffar)'s block [Building a PinEntryEditText in Android](https://medium.com/@ali.muzaffar/building-a-pinentryedittext-in-android-5f2eddcae5d3). Please check that out.

## SetUp

PinEntryEditText required **minSdkVersion 21** and **androidx**


Add it in your project level build.gradle at the end of repositories:
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Add the dependency in your app.gradle file:
```
dependencies {
  implementation 'com.github.fahim44:PinEntryEditText:{latest_version}'
}
```


## Example

In your xml, add :

```
<!--
     * Set android:maxLength for set Total field count,Default value is 4
    -->
    <com.lamonjush.pinentryedittext.PinEntryEditText
        android:id="@+id/pinEntryEditText"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_margin="10dp"
        android:backgroundTint="@android:color/transparent"
        android:digits="1234567890"
        android:inputType="number"
        android:maxLength="4"
        android:textColor="@android:color/black"
        android:textSize="34sp"
        app:focusedStateLineColor="@android:color/holo_blue_dark"
        app:innerColor="@android:color/white"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:lineCornerRadius="8dp"
        app:lineWidth="3dp"
        app:selectedStateLineColor="@android:color/holo_red_dark"
        app:unFocusedStateLineColor="@android:color/darker_gray" />
```

to get pinEntryCallback, setUp callback in your View Java class:

```
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PinEntryEditText pinEntryEditText = findViewById(R.id.pinEntryEditText);
        pinEntryEditText.setPinEntryListener(
                new PinEntryListener() {
                    @Override
                    public void onPinEntered(String pin) {
                        Toast.makeText(MainActivity.this,pin,Toast.LENGTH_SHORT);
                    }
                }
        );
    }
}
```

## Custom Attributes

PinEntryEditText support all normal editText attributes, It also supports following additional attributes:

```
<!-- selected field Border color, activated when focused, default value is equal to focusedStateLineColor -->
<attr name="selectedStateLineColor" format="color" />

<!-- unSelected field Border color, activated when focused, default value is equal to unFocusedStateLineColor -->
<attr name="focusedStateLineColor" format="color" />

<!-- field Border color when not focused, default value is GREY -->
<attr name="unFocusedStateLineColor" format="color" />

<!-- inner box color, default value is TRANSPARENT -->
<attr name="innerColor" format="color" />

<!-- box border corner radius, default value is 4dp -->
<attr name="lineCornerRadius" format="dimension" />

<!-- box border width, default value is 2dp -->
<attr name="lineWidth" format="dimension" />
 ```
