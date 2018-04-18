import 'dart:async';

import 'package:flutter/services.dart';

class FireFunctions {
  static const MethodChannel _channel =
      const MethodChannel('fire_functions');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<Map<String, dynamic>> callFunction(String functionName, Map<String, dynamic> data) async {
    final result = await _channel.invokeMethod('callFunction', data);
    return result;
  }
}
