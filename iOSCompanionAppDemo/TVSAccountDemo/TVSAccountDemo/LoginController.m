//
//  ViewController.m
//  TvsLoginDemo
//
//  Created by ZACARDFANG on 2017/8/11.
//  Copyright © 2017年 tencent. All rights reserved.
//

#import "LoginController.h"

#import <TVSAccountSDK/TVSAccountSDK.h>

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
    //测试环境
    [[TVSAccountSDK shared]setTestEnvironment:YES];
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
    [[TVSAccountSDK shared]wxLoginWithHandler:^(BOOL success){
        [self refreshBtnStatus];
        [self textViewAppendText:[NSString stringWithFormat:@"微信登录%@", success ? @"成功" : @"失败"]];
    }];
}

//刷新微信token(已登录情况下)
- (IBAction)onClickBtnWXToken:(id)sender {
    [[TVSAccountSDK shared]wxTokenRefreshWithHandler:^(BOOL success){
        [self refreshBtnStatus];
        [self textViewAppendText:[NSString stringWithFormat:@"微信token刷新%@", success ? @"成功" : @"失败"]];
    }];
}

//调用微信支付
- (IBAction)onClickWXPay:(id)sender {
    [[TVSAccountSDK shared]wxPayWithPartnerid:@"partnerid" prepayid:@"prepayid" package:@"package" noncestr:@"noncestr" sign:@"sign" timestamp:123456789 handler:^(BOOL success, NSString* key){
        if (success) {
            [self textViewAppendText:[NSString stringWithFormat:@"微信支付成功：%@", key]];
        } else {
            [self textViewAppendText:@"微信支付失败"];
        }
    }];
}

//调用QQ登录
- (IBAction)onClickQQLogin:(id)sender {
    [[TVSAccountSDK shared]qqLoginWithHandler:^(BOOL success){
        [self refreshBtnStatus];
        [self textViewAppendText:[NSString stringWithFormat:@"QQ登录%@",  success ? @"成功" : @"失败"]];
    }];
}

//验证QQ token(已登录情况下)
- (IBAction)onClickQQToken:(id)sender {
    [[TVSAccountSDK shared]qqTokenVerifyWithHandler:^(BOOL success){
        [self refreshBtnStatus];
        [self textViewAppendText:[NSString stringWithFormat:@"QQ token验证%@",  success ? @"成功" : @"失败"]];
    }];
}

//注销登录
- (IBAction)onClickBtnLogout:(id)sender {
    [[TVSAccountSDK shared]logout];
    [self refreshBtnStatus];
}

//进入个人中心页面
- (IBAction)onClickBtnUserCenter:(id)sender {
    [[TVSAccountSDK shared]enterUserCenterPageFromViewController:self];

//    if ([TVSAccountSDK shared].isWXTokenExist || [TVSAccountSDK shared].isQQTokenExist) {//必须先登录
//        NSString* phone= @"13987654321";
//
//        //获取用于绑定手机号的验证码
//        [[TVSAccountSDK shared]getCaptchaWithPhoneNumber:phone handler:^(BOOL success){
//            [self textViewAppendText:[NSString stringWithFormat:@"获取验证码%@",  success ? @"成功" : @"失败"]];
//        }];
//
//        //绑定手机号
//        [[TVSAccountSDK shared]bindPhoneNumber:phone captcha:@"252823" handler:^(BOOL success){
//            [self textViewAppendText:[NSString stringWithFormat:@"绑定手机号%@",  success ? @"成功" : @"失败"]];
//        }];
//
//        //绑定地址
//        TVSGeoLocation* loc = [TVSGeoLocation new];
//        loc.name = @"大族激光科技中心大厦";
//        loc.addr = @"深南大道9988号";
//        loc.lat = @"23.0";
//        loc.lng = @"80.0";
//        [[TVSAccountSDK shared]bindHomeLocation:loc companyLocation:loc handler:^(BOOL success){
//            [self textViewAppendText:[NSString stringWithFormat:@"绑定地址%@",  success ? @"成功" : @"失败"]];
//        }];
//
//        //查询地址
//        [[TVSAccountSDK shared]queryLocationWithHandler:^(NSArray* homeLocations, NSArray* companyLocations){
//
//        }];
//    }
}

-(UIButton*)ButtonWithFrame:(CGRect)frame text:(NSString*)text selector:(SEL)selector {
    UIButton* btn = [[UIButton alloc]initWithFrame:frame];
    [btn setTitleColor:self.view.tintColor forState:UIControlStateNormal];
    [btn setTitleColor:[UIColor grayColor] forState:UIControlStateDisabled];
    [btn.titleLabel setFont:[UIFont systemFontOfSize:15]];
    [btn addTarget:self action:selector forControlEvents:UIControlEventTouchUpInside];
    [btn setTitle:text forState:UIControlStateNormal];
    return btn;
}

-(void)refreshBtnStatus {
    if ([[TVSAccountSDK shared]isWXTokenExist]) {//是否微信登录
        _btnWXLogin.enabled = NO;
        _btnWXToken.enabled = YES;
        _btnQQLogin.enabled = NO;
        _btnQQToken.enabled = NO;
        _btnLogout.enabled = YES;
    } else if ([[TVSAccountSDK shared]isQQTokenExist]) {//是否QQ登录
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

-(void)textViewAppendText:(NSString*)text {
    if (_tvResult.text == nil || _tvResult.text.length == 0) {
        _tvResult.text = text;
    } else {
        _tvResult.text = [_tvResult.text stringByAppendingString:[NSString stringWithFormat:@"%@%@", @"\n\n", text]];
    }
}

@end
