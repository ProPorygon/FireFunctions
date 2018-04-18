package com.proporygon.firefunctions;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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

  private Task<Map> functionTask(String functionName, Map<String, Object> data) {
    return mFunctions.getHttpsCallable(functionName)
            .call(data)
            .continueWith(new Continuation<HttpsCallableResult, Map>() {
              @Override
              public Map then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                if (!task.isSuccessful()) {
                  Exception e = task.getException();
                  if (e instanceof FirebaseFunctionsException) {
                    FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                    FirebaseFunctionsException.Code code = ffe.getCode();
                    Object details = ffe.getDetails();
                    return new HashMap();
                  }
                }
                Map<String, Object> result = (Map) task.getResult().getData();
                return result;
              }
            });
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
      String functionName = call.method;
      Map<String, Object> data = (Map) call.arguments;
      try {
        Map functionResult = Tasks.await(functionTask(functionName, data));
        result.success(functionResult);
      } catch (ExecutionException e) {
        Log.d("FireFunctionsPlugin", e.getMessage());
      } catch (InterruptedException e) {
        Log.d("FireFunctionsPlugin", e.getMessage());
      }
  }
}
