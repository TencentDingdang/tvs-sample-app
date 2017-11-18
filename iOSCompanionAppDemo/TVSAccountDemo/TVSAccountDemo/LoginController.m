//
//  ViewController.m
//  TvsLoginDemo
//
//  Created by ZACARDFANG on 2017/8/11.
//  Copyright © 2017年 tencent. All rights reserved.
//

#import "LoginController.h"

#import <TVSAccountSDK/TVSAccount.h>
#import <TVSAccountSDK/TVSMember.h>

@interface LoginController()
@property (strong, nonatomic) IBOutlet UIButton *btnWXLogin;
@property (strong, nonatomic) IBOutlet UIButton *btnWXToken;
@property (strong, nonatomic) IBOutlet UIButton *btnQQLogin;
@property (strong, nonatomic) IBOutlet UIButton *btnQQToken;
@property (strong, nonatomic) IBOutlet UITextView *tvResult;
@property (strong, nonatomic) IBOutlet UIButton *btnLogout;

@end

@implementation LoginController

- (void)viewDidLoad {
    [super viewDidLoad];
}

-(void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self refreshBtnStatus];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

//调用微信登录
- (IBAction)onClickBtnWXLogin:(id)sender {
    [[TVSAccount shared]wxLoginWithHandler:^(BOOL success){
        [self showText:[NSString stringWithFormat:@"微信登录%@", success ? @"成功" : @"失败"]];
        if (success) {
            [self readAccountUserInfo];
        }
        [self refreshBtnStatus];
    }];
}

//刷新微信token(已登录情况下)
- (IBAction)onClickBtnWXToken:(id)sender {
    [[TVSAccount shared]wxTokenRefreshWithHandler:^(BOOL success){
        [self showText:[NSString stringWithFormat:@"微信 token 刷新%@", success ? @"成功" : @"失败"]];
        if (success) {
            [self readAccountUserInfo];
        }
        [self refreshBtnStatus];
    }];
}

//调用微信支付
- (IBAction)onClickWXPay:(id)sender {
    [[TVSAccount shared]wxPayWithPartnerid:@"partnerid" prepayid:@"prepayid" package:@"package" noncestr:@"noncestr" sign:@"sign" timestamp:123456789 handler:^(BOOL success, NSString* key){
        if (success) {
            [self showText:[NSString stringWithFormat:@"微信支付成功：%@", key]];
        } else {
            [self showText:@"微信支付失败"];
        }
    }];
}

//调用QQ登录
- (IBAction)onClickQQLogin:(id)sender {
    [[TVSAccount shared]qqLoginWithHandler:^(BOOL success){
        [self showText:[NSString stringWithFormat:@"QQ 登录%@",  success ? @"成功" : @"失败"]];
        if (success) {
            [self readAccountUserInfo];
        }
        [self refreshBtnStatus];
    }];
}

//验证QQ token(已登录情况下)
- (IBAction)onClickQQToken:(id)sender {
    [[TVSAccount shared]qqTokenVerifyWithHandler:^(BOOL success){
        [self showText:[NSString stringWithFormat:@"QQ token 验证%@",  success ? @"成功" : @"失败"]];
        if (success) {
            [self readAccountUserInfo];
        }
        [self refreshBtnStatus];
    }];
}

//读取账号、用户信息
-(void)readAccountUserInfo {
    TVSAccountInfo* ai = [TVSAccount shared].accountInfo;
    TVSUserInfo* ui = [TVSAccount shared].userInfo;
    NSString* clientId = [TVSAccountInfo clientIdWithDSN:@"mDSN" productId:@"mProductId"];
    [self showText:[NSString stringWithFormat:@"nickname: %@",  ui.nickName]];
    [self showText:[NSString stringWithFormat:@"token: %@",  ai.accessToken]];
    [self showText:[NSString stringWithFormat:@"ClientId: %@",  clientId]];
}

//注销登录
- (IBAction)onClickBtnLogout:(id)sender {
    [[TVSAccount shared]logout];
    [self refreshBtnStatus];
    _tvResult.text = nil;
}

//进入会员H5页面
- (IBAction)onClickBtnUserCenter:(id)sender {
    TVSMember* uc = [[TVSMember alloc]initWithDeviceId:@"mDeviceId" deviceType:@"mDeviceType" deviceOEM:@"mDeviceOEM"];
    [uc enterPageFromViewController:self];

//    if ([TVSAccount shared].isWXTokenExist || [TVSAccount shared].isQQTokenExist) {//必须先登录
//        NSString* phone= @"13987654321";
//
//        //获取用于绑定手机号的验证码
//        [[TVSAccount shared]getCaptchaWithPhoneNumber:phone handler:^(BOOL success){
//            [self textViewAppendText:[NSString stringWithFormat:@"获取验证码%@",  success ? @"成功" : @"失败"]];
//        }];
//
//        //绑定手机号
//        [[TVSAccount shared]bindPhoneNumber:phone captcha:@"252823" handler:^(BOOL success){
//            [self textViewAppendText:[NSString stringWithFormat:@"绑定手机号%@",  success ? @"成功" : @"失败"]];
//        }];
//
//        //绑定地址
//        TVSGeoLocation* loc = [TVSGeoLocation new];
//        loc.name = @"大族激光科技中心大厦";
//        loc.addr = @"深南大道9988号";
//        loc.lat = @"23.0";
//        loc.lng = @"80.0";
//        [[TVSAccount shared]bindHomeLocation:loc companyLocation:loc handler:^(BOOL success){
//            [self textViewAppendText:[NSString stringWithFormat:@"绑定地址%@",  success ? @"成功" : @"失败"]];
//        }];
//
//        //查询地址
//        [[TVSAccount shared]queryLocationWithHandler:^(NSArray* homeLocations, NSArray* companyLocations){
//
//        }];
//    }
}

-(void)refreshBtnStatus {
    if ([[TVSAccount shared]isWXTokenExist]) {//是否微信登录
        _btnWXLogin.enabled = NO;
        _btnWXToken.enabled = YES;
        _btnQQLogin.enabled = NO;
        _btnQQToken.enabled = NO;
        _btnLogout.enabled = YES;
    } else if ([[TVSAccount shared]isQQTokenExist]) {//是否QQ登录
        _btnWXLogin.enabled = NO;
        _btnWXToken.enabled = NO;
        _btnQQLogin.enabled = NO;
        _btnQQToken.enabled = YES;
        _btnLogout.enabled = YES;
    } else {//未登录
        _btnWXLogin.enabled = YES;
        _btnWXToken.enabled = NO;
        _btnQQLogin.enabled = YES;
        _btnQQToken.enabled = NO;
        _btnLogout.enabled = NO;
    }
}

-(void)showText:(NSString*)text {
    if (_tvResult.text == nil || _tvResult.text.length == 0) {
        _tvResult.text = text;
    } else {
        _tvResult.text = [_tvResult.text stringByAppendingString:[NSString stringWithFormat:@"%@%@", @"\n\n", text]];
    }
    if (_tvResult.text.length > 0) {
        NSRange bottom = NSMakeRange(_tvResult.text.length - 1, 1);
        [_tvResult scrollRangeToVisible:bottom];
    }
}

@end
