package com.proporygon.firefunctions;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Map;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;


/**
 * FireFunctionsPlugin
 */
public class FireFunctionsPlugin implements MethodCallHandler {
  private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();

  /**
   * Plugin registration.
   */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "fire_functions");
    channel.setMethodCallHandler(new FireFunctionsPlugin());
  }

  private Task<Object> functionTask(String functionName, Map<String, Object> data) {
    return mFunctions.getHttpsCallable(functionName)
            .call(data)
            .continueWith(new Continuation<HttpsCallableResult, Object>() {
              @Override
              public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                if (!task.isSuccessful()) {
                  Exception e = task.getException();
                  if (e instanceof FirebaseFunctionsException) {
                    FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                    FirebaseFunctionsException.Code code = ffe.getCode();
                    Object details = ffe.getDetails();
                    Log.d("FireFunctionsPlugin", code.name());
                    throw e;
                  }
                }
                return task.getResult().getData();
              }
            });
  }

  @Override
  public void onMethodCall(MethodCall call, final Result result) {
      String functionName = call.method;
      Map<String, Object> data = (Map) call.arguments;
      Task<Object> task = functionTask(functionName, data);
      task.addOnSuccessListener(new OnSuccessListener<Object>() {
          @Override
          public void onSuccess(Object resultData) {
              result.success(resultData);
          }
      });
      task.addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
              Log.d("FunctionFailed", e.getMessage());
              e.printStackTrace();
          }
      });
  }
}
