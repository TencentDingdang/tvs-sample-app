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
@property(nonatomic,strong) UIButton *btnWXLogin, *btnQQLogin, *btnWXRefresh, *btnQQVerify, *btnLogout, *btnUserCenter;
@property(nonatomic,strong) UITextView *textview;
@end

@implementation LoginController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    CGFloat W = [UIScreen mainScreen].bounds.size.width;
    CGFloat H = [UIScreen mainScreen].bounds.size.height;
    CGFloat Y = [[UIApplication sharedApplication] statusBarFrame].size.height + self.navigationController.navigationBar.frame.size.height;
    
    _btnWXLogin = [self ButtonWithFrame:CGRectMake(0, Y+5, W/2, 30) Text:@"微信登录" Selector:@selector(onClickWXLogin:)];
    [self.view addSubview:_btnWXLogin];
    
    _btnWXRefresh = [self ButtonWithFrame:CGRectMake(W/2, Y+5, W/2, 30) Text:@"刷新微信 Token" Selector:@selector(onClickWXRefresh:)];
    [self.view addSubview:_btnWXRefresh];
    
    Y += _btnWXLogin.frame.size.height;
    
    _btnQQLogin = [self ButtonWithFrame:CGRectMake(0, Y+5, W/2, 30) Text:@"QQ 登录" Selector:@selector(onClickQQLogin:)];
    [self.view addSubview:_btnQQLogin];
    
    _btnQQVerify = [self ButtonWithFrame:CGRectMake(W/2, Y+5, W/2, 30) Text:@"验证 QQ Token" Selector:@selector(onClickQQVerify:)];
    [self.view addSubview:_btnQQVerify];
    
    Y += _btnQQLogin.frame.size.height;
    
    _btnLogout = [self ButtonWithFrame:CGRectMake(0, Y+5, W/2, 30) Text:@"注销登录" Selector:@selector(onClickLogout:)];
    [self.view addSubview:_btnLogout];
    
    _btnUserCenter = [self ButtonWithFrame:CGRectMake(W/2, Y+5, W/2, 30) Text:@"个人中心" Selector:@selector(onClickUserCenter:)];
    [self.view addSubview:_btnUserCenter];
    
    Y += _btnLogout.frame.size.height;
    
    [self refreshBtnStatus];
    
    _textview = [[UITextView alloc]initWithFrame:CGRectMake(10, Y + 10, W - 20, H - Y - 20)];
    _textview.backgroundColor = [UIColor blackColor];
    _textview.layer.cornerRadius = 5;
    [_textview setFont:[UIFont systemFontOfSize:13]];
    [_textview setTextColor:[UIColor greenColor]];
    _textview.editable = NO;
    [self.view addSubview:_textview];
    
    //测试环境
    [[TVSAccountSDK shared]setTestEnvironment:YES];
    //接收 TVS 事件通知
    TVSNotificationAddObserver(@selector(handleTVSNotification:));
}

//调用微信登录
-(void)onClickWXLogin:(id)sender {
    [[TVSAccountSDK shared]loginWithWX];
}

//刷新微信token(已登录情况下)
-(void)onClickWXRefresh:(id)sender {
    [[TVSAccountSDK shared]refreshWXToken];
}

//调用QQ登录
-(void)onClickQQLogin:(id)sender {
    [[TVSAccountSDK shared]loginWithQQ];
}

//验证QQ token(已登录情况下)
-(void)onClickQQVerify:(id)sender {
    [[TVSAccountSDK shared]verifyQQToken];
}

//注销登录
-(void)onClickLogout:(id)sender {
    [[TVSAccountSDK shared]logout];
    [self refreshBtnStatus];
}

-(void)onClickUserCenter:(id)sender {
    //进入个人中心页面
    [[TVSAccountSDK shared]enterUserCenterPageFromViewController:self];
    
    if ([TVSAccountSDK shared].isWXTokenExist || [TVSAccountSDK shared].isQQTokenExist) {//必须先登录
        //获取用于绑定手机号的验证码
        [[TVSAccountSDK shared]getCaptchaWithPhoneNumber:@"13987654321"];
        
        //绑定手机号
        [[TVSAccountSDK shared]bindPhoneNumber:@"13987654321" Captcha:@"123456"];
        
        //绑定地址
        TVSGeoLocation* loc = [TVSGeoLocation new];
        loc.name = @"大族激光科技中心大厦";
        loc.addr = @"深南大道9988号";
        loc.lat = @"23.0";
        loc.lng = @"80.0";
        [[TVSAccountSDK shared]bindHomeLocation:loc CompanyLocation:loc];
        
        //查询地址
        [[TVSAccountSDK shared]queryLocation];
    }
}

-(void)viewDidDisappear:(BOOL)anim {
    //不再接收 TVS 事件通知
    TVSNotificationRemoveObserver();
    [super viewDidDisappear:anim];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

-(UIButton*)ButtonWithFrame:(CGRect)frame Text:(NSString*)text Selector:(SEL)selector {
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
        _btnWXRefresh.enabled = YES;
        _btnQQLogin.enabled = NO;
        _btnQQVerify.enabled = NO;
        _btnLogout.enabled = YES;
    } else if ([[TVSAccountSDK shared]isQQTokenExist]) {//是否QQ登录
        _btnWXLogin.enabled = NO;
        _btnWXRefresh.enabled = NO;
        _btnQQLogin.enabled = NO;
        _btnQQVerify.enabled = YES;
        _btnLogout.enabled = YES;
    } else {//未登录
        _btnWXLogin.enabled = YES;
        _btnWXRefresh.enabled = NO;
        _btnQQLogin.enabled = YES;
        _btnQQVerify.enabled = NO;
        _btnLogout.enabled = NO;
    }
}

-(void)textViewAppendText:(NSString*)text {
    if (_textview.text == nil || _textview.text.length == 0) {
        _textview.text = text;
    } else {
        _textview.text = [_textview.text stringByAppendingString:[NSString stringWithFormat:@"%@%@", @"\n\n", text]];
    }
}

-(void)handleTVSNotification:(NSNotification*)notify {
    TVSAccountEvent event = TVSNotificationGetEvent(notify);
    BOOL success = TVSNotificationGetSuccess(notify);
    [self refreshBtnStatus];
    switch (event) {
        case TVSAccountEventFetchWXToken: {
            [self textViewAppendText:[NSString stringWithFormat:@"获取微信 Token %@", success ? @"成功" : @"失败"]];
            break;
        }
        case TVSAccountEventRefreshWXToken: {
            [self textViewAppendText:[NSString stringWithFormat:@"刷新微信 Token %@", success ? @"成功" : @"失败"]];
            break;
        }
        case TVSAccountEventVerifyQQToken: {
            [self textViewAppendText:[NSString stringWithFormat:@"验证 QQ Token %@", success ? @"成功" : @"失败"]];
            break;
        }
        case TVSAccountEventFetchTvsId: {
            TVSAccountInfo* accountInfo = [TVSAccountSDK shared].accountInfo;//读取账号信息
            TVSUserInfo* userInfo = [TVSAccountSDK shared].userInfo;//读取用户资料
            [self textViewAppendText:[NSString stringWithFormat:@"%@%@", success ? @"获取 ClientId 成功:\n" : @"获取 ClientId 失败:\n", accountInfo.clientId]];
            break;
        }
        case TVSAccountEventGetCaptcha: {
            [self textViewAppendText:[NSString stringWithFormat:@"获取验证码:%d", success]];
            break;
        }
        case TVSAccountEventBindPhoneNumber: {
            [self textViewAppendText:[NSString stringWithFormat:@"绑定手机号:%d", success]];
            break;
        }
        case TVSAccountEventBindLocation: {
            [self textViewAppendText:[NSString stringWithFormat:@"绑定地址:%d", success]];
            break;
        }
        case TVSAccountEventQueryLocation: {
            [self textViewAppendText:[NSString stringWithFormat:@"地址查询:%d", success]];
            if (success) {
                TVSGeoLocation* homeLoc = [[TVSAccountSDK shared]userInfo].homeLocation;
                TVSGeoLocation* compoanyLoc = [[TVSAccountSDK shared]userInfo].companyLocation;
            }
            break;
        }
    }
}

@end
