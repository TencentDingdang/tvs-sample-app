/**
 * Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * You may not use this file except in compliance with the License. A copy of the License is located the "LICENSE.txt"
 * file accompanying this source. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */

#import <LoginWithAmazon/LoginWithAmazon.h>
#import "LoginController.h"
#import "ProvisioningClient.h"

@interface LoginController()
    @property (strong, nonatomic) NSString *sessionId;
    @property (strong, nonatomic) NSString *productId;
    @property (strong, nonatomic) NSString *dsn;
    @property (strong, nonatomic) NSString *codeChallenge;
    @property (strong, nonatomic) NSString *authCode;
    @property (strong, nonatomic) ProvisioningClient* deviceProvisionClient;
@end

@implementation LoginController

- (IBAction)onConnectButtonClicked:(id)sender {
    [self.deviceAddress resignFirstResponder];

    [self clearLog];
    NSString *address = _deviceAddress.text;
    [self appendLog:[NSString stringWithFormat:@"开始连接设备, address : %@", address]];
    
    [self.deviceProvisionClient getDeviceProvisioningInfo: address];
    
    self.deviceSearchProgress.hidden = NO;
    [self.deviceSearchProgress startAnimating];
    self.connectToDeviceButton.hidden = YES;
    [AIMobileLib clearAuthorizationState:nil];

    _loginPanel.hidden = YES;
}

#pragma connect device sucess
- (void) deviceDiscovered : (AVSDeviceResponse *) response {
    [self.deviceSearchProgress stopAnimating];
    self.deviceSearchProgress.hidden = YES;
    self.connectToDeviceButton.hidden = NO;
    self.sessionId = response.sessionId;
    self.productId = response.productId;
    self.dsn = response.dsn;
    self.codeChallenge = response.codeChallenge;
    
    [self appendLog:@"开始连接设备成功..."];
    
    //设备连接成功后，如果已经有Toekn就直接刷新Toekn
    if ([[TVSAuth shared] isWXTokenExist]) {
        [self appendLog:@"微信已登录，刷新Token..."];
        _loginType = @"wx";
        [[TVSAuth shared] loginWithWX];
        return;
    }
    
    _loginPanel.hidden = NO;
}

#pragma connect device fail
- (void) errorSearchingForDevice: (NSError*) error {
    [self appendLog:[NSString stringWithFormat:@"Searching device fail, error = %@", [error localizedDescription]]];
}

#pragma postCompanionProvisioningInfo success
- (void) deviceSuccessfulyProvisioned {
    //Your device is now successfully provisioned and ready to use.
    [self appendLog:@"post provision success, Your device is now successfully provisioned and ready to use."];
}

#pragma postCompanionProvisioningInfo fail
- (void) errorProvisioningDevice: (NSError*) error {
    [self appendLog:[NSString stringWithFormat:@"post provision fail, error = %@", [error localizedDescription]]];
}

- (IBAction) onLogInButtonClicked:(id)sender {
    [self appendLog:@"amazon login..."];
    
    _loginType = @"amazon";
    
    NSDictionary *scopeData = @{@"productID": self.productId,
                                @"productInstanceAttributes": @{@"deviceSerialNumber": self.dsn}};

    id<AMZNScope> alexaAllScope = [AMZNScopeFactory scopeWithName:@"alexa:all" data:scopeData];

    AMZNAuthorizeRequest *request = [[AMZNAuthorizeRequest alloc] init];
    request.scopes = @[alexaAllScope];
    request.codeChallenge = self.codeChallenge;
    request.codeChallengeMethod = @"S256";
    request.grantType = AMZNAuthorizationGrantTypeCode;

    AMZNAuthorizationManager *authManager = [AMZNAuthorizationManager sharedManager];
    [authManager authorize:request withHandler:^(AMZNAuthorizeResult *result, BOOL userDidCancel, NSError *error) {
        if (error) {
            // Notify the user that authorization failed
            [self appendLog:[NSString stringWithFormat:@"User authorization failed due to an error: %@", error.localizedDescription]];
        } else if (userDidCancel) {
            [self appendLog:@"Authorization was cancelled prior to completion. To continue, you will need to try logging in again."];
        } else {
            // Fetch the authorization code and return to controller
            self.authCode = result.authorizationCode;
            
            [self appendLog:@"amazon login success..."];

            [self userSuccessfullySignedIn];
        }
    }];
}


- (IBAction)onWxLoginButtonClick:(id)sender {
    [self appendLog:@"微信 login..."];
    
    _loginType = @"wx";
    // 请求登录
    [[TVSAuth shared] loginWithWX];
}

- (IBAction)onQQLoginButtonClick:(id)sender {
    [self appendLog:@"QQ login..."];
    
    _loginType = @"qq";
}

-(void) userSuccessfullySignedIn {
    [self appendLog:@"postCompanionProvisioningInfo ..."];

    NSString *authCode;
    NSString *clientId;
    NSString *redirectUri;

    if ([@"amazon" isEqualToString:_loginType]) {
        authCode = self.authCode;
        clientId = [AIMobileLib getClientId];
        redirectUri = [AIMobileLib getRedirectUri];
    } else if ([@"qq" isEqualToString:_loginType]) {
        
    } else if ([@"wx" isEqualToString:_loginType]) {
        authCode = @"authCode";
        clientId = [[TVSAuth shared] clientId];
        redirectUri = @"redirectUri";
    }
    
    [self appendLog:[NSString stringWithFormat:@"params -> address:%@\nauthCode:%@clientId:%@redirectUri:%@sessionId:%@",
                     self.deviceAddress.text,authCode,clientId,redirectUri,self.sessionId]];
    
    [self.deviceProvisionClient postCompanionProvisioningInfo:self.deviceAddress.text
                                                     authCode:authCode
                                                     clientId:clientId
                                                  redirectUri:redirectUri
                                                    sessionId:self.sessionId];
}

#pragma mark View controller specific functions
- (BOOL)shouldAutorotate {
    return NO;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self clearLog];
    
    self.loginPanel.hidden = YES;
    self.deviceSearchProgress.hidden = YES;

    self.deviceProvisionClient = [[ProvisioningClient alloc] initWithDelegate:self];

    float systemVersion=[[[UIDevice currentDevice] systemVersion] floatValue];
    if(systemVersion>=7.0f)
    {
        CGRect tempRect;
        for(UIView *sub in [[self view] subviews])
        {
            tempRect = [sub frame];
            tempRect.origin.y += 20.0f; //Height of status bar
            [sub setFrame:tempRect];
        }
    }
    
    [[TVSAuth shared] setAuthDelegate:self];
}


#pragma mark TVSAuthDelegate

- (void)TVSAuthEvent:(TVSAuthEvent)event Success:(BOOL)success {
    switch (event) {
        case TVSAuthEventFetchId: {
            NSString* clientId = [[TVSAuth shared]clientId];
            [self appendLog:[NSString stringWithFormat:@"%@%@", success ? @"获取 ClientId 成功:\n" : @"获取 ClientId 失败:\n", clientId]];
            [self userSuccessfullySignedIn];
            break;
        }
        case TVSAuthEventRefreshWXToken: {
            [self appendLog:[NSString stringWithFormat:@"获取微信 Token %@", success ? @"成功" : @"失败"]];
            break;
        }
        case TVSAuthEventRefreshWXToken: {
            [self appendLog:[NSString stringWithFormat:@"刷新微信 Token %@", success ? @"成功" : @"失败"]];
            break;
        }
    }
}

- (void)clearLog{
    _logPanel.text = @"";
}

- (void)appendLog:(NSString *)text{
    NSMutableString *str = [[NSMutableString alloc] initWithString:_logPanel.text];
    [str appendFormat:@"\n%@", text];
    _logPanel.text = str;
}

@end
