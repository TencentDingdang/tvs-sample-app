//
//  ViewController.m
//  TvsLoginDemo
//
//  Created by ZACARDFANG on 2017/8/11.
//  Copyright © 2017年 tencent. All rights reserved.
//

#import "LoginController.h"

#import <TvsLoginSdk/TVSAuth.h>
#import <TvsLoginSdk/TVSAuthDelegate.h>

@interface LoginController()<TVSAuthDelegate>
@property(nonatomic,strong) UIButton *btnWXLogin, *btnQQLogin, *btnWXRefresh, *btnQQVerify, *btnLogout;
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
    
    _btnLogout = [self ButtonWithFrame:CGRectMake(0, Y+5, W, 30) Text:@"注销登录" Selector:@selector(onClickLogout:)];
    [self.view addSubview:_btnLogout];
    
    Y += _btnLogout.frame.size.height;
    
    [self refreshBtnStatus];
    
    _textview = [[UITextView alloc]initWithFrame:CGRectMake(10, Y + 10, W - 20, H - Y - 20)];
    _textview.backgroundColor = [UIColor blackColor];
    _textview.layer.cornerRadius = 5;
    [_textview setFont:[UIFont systemFontOfSize:13]];
    [_textview setTextColor:[UIColor greenColor]];
    _textview.editable = NO;
    [self.view addSubview:_textview];
    
    [[TVSAuth shared]setAuthDelegate:self];
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
    if ([[TVSAuth shared]isWXTokenExist]) {
        _btnWXLogin.enabled = NO;
        _btnWXRefresh.enabled = YES;
        _btnQQLogin.enabled = NO;
        _btnQQVerify.enabled = NO;
        _btnLogout.enabled = YES;
    } else if ([[TVSAuth shared]isQQTokenExist]) {
        _btnWXLogin.enabled = NO;
        _btnWXRefresh.enabled = NO;
        _btnQQLogin.enabled = NO;
        _btnQQVerify.enabled = YES;
        _btnLogout.enabled = YES;
    } else {
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

-(void)onClickWXLogin:(id)sender {
    [[TVSAuth shared]loginWithWX];
}

-(void)onClickWXRefresh:(id)sender {
    [[TVSAuth shared]refreshWXToken];
}

-(void)onClickQQLogin:(id)sender {
    [[TVSAuth shared]loginWithQQ];
}

-(void)onClickQQVerify:(id)sender {
    [[TVSAuth shared]verifyQQToken];
}

-(void)onClickLogout:(id)sender {
    [[TVSAuth shared]logout];
    [self refreshBtnStatus];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

#pragma mark TVSAuthDelegate

-(void)TVSAuthEvent:(TVSAuthEvent)event Success:(BOOL)success {
    [self refreshBtnStatus];
    switch (event) {
        case TVSAuthEventFetchWXToken: {
            [self textViewAppendText:[NSString stringWithFormat:@"获取微信 Token %@", success ? @"成功" : @"失败"]];
            break;
        }
        case TVSAuthEventRefreshWXToken: {
            [self textViewAppendText:[NSString stringWithFormat:@"刷新微信 Token %@", success ? @"成功" : @"失败"]];
            break;
        }
        case TVSAuthEventVerifyQQToken: {
            [self textViewAppendText:[NSString stringWithFormat:@"验证 QQ Token %@", success ? @"成功" : @"失败"]];
            break;
        }
        case TVSAuthEventFetchId: {
            NSString* clientId = [[TVSAuth shared]clientId];
            UserInfo* ui = [[TVSAuth shared]userInfo];
            if (ui);
            [self textViewAppendText:[NSString stringWithFormat:@"%@%@", success ? @"获取 ClientId 成功:\n" : @"获取 ClientId 失败:\n",clientId]];
            break;
        }
    }
}

@end
