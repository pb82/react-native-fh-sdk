
# react-native-fh-sdk

## Getting started

`$ npm install react-native-fh-sdk --save`

### Mostly automatic installation

`$ react-native link react-native-fh-sdk`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-fh-sdk` and add `RNFhSdk.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNFhSdk.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.feedhenry.RNFhSdkPackage;` to the imports at the top of the file
  - Add `new RNFhSdkPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-fh-sdk'
  	project(':react-native-fh-sdk').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-fh-sdk/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-fh-sdk')
  	```


## Usage
```javascript
import RNFhSdk from 'react-native-fh-sdk';

// TODO: What to do with the module?
RNFhSdk;
```
  