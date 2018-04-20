import 'dart:async';

import 'package:flutter/services.dart';

class FireFunctions {
  static const MethodChannel _channel =
      const MethodChannel('fire_functions');

  static Future callFunction(String functionName, Map<String, dynamic> data) async {
    final result = await _channel.invokeMethod(functionName, data);
    return result;
  }
}
