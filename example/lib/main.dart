import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fire_functions/fire_functions.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  initPlatformState() async {
    Map result;
    // Platform messages may fail, so we use a try/catch PlatformException.
    const Map<String, dynamic> data = {
      "latitude": 20.0,
      "longitude": 20.0,
      "name": "Test",
      "region": "test",
    };
    try {
      result = await FireFunctions.callFunction("addCourt", data);
      print(result);
    } on PlatformException {
      print("Call Failed");
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted)
      return;

    setState(() {
    });
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text('Plugin example app'),
        ),
        body: new Center(
          child: new Text('Testing'),
        ),
      ),
    );
  }
}
