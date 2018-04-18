#import "FireFunctionsPlugin.h"
#import <fire_functions/fire_functions-Swift.h>

@implementation FireFunctionsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFireFunctionsPlugin registerWithRegistrar:registrar];
}
@end
