//
//  TVSAccountSDK.h
//  TVSAccountSDK
//
//  Created by RincLiu on 21/08/2017.
//  Copyright © 2017 tencent. All rights reserved.
//

/*!
 * @file TVSAccountSDK.h
 */
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

/*!
 * @brief 非法的 ClientId 常量
 */
extern NSString* const TVSInvalidClientId;

/*!
 * @brief 非法的 RefreshToken 常量
 */
extern NSString* const TVSInvalidRefreshToken;

/*!
 * @brief TVS 通知名
 */
extern NSString* const TVSNotificationName;

/*!
 * @brief TVS 通知 事件
 */
extern NSString* const TVSNotificationKey_EVENT;

/*!
 * @brief TVS 通知 结果
 */
extern NSString* const TVSNotificationKey_SUCCESS;



/*!
 * @brief 接收 TVS 事件通知
 * @warning 必须注册才能接收 TVS 事件回调
 * @param sel 通知处理方法 selector
 */
#define TVSNotificationAddObserver(sel) [[NSNotificationCenter defaultCenter]addObserver:self selector:sel name:TVSNotificationName object:nil]

/*!
 * @brief 不再接收 TVS 事件通知
 * @warning 请在必要的时候（如viewDidDisappear）取消注册
 */
#define TVSNotificationRemoveObserver() [[NSNotificationCenter defaultCenter]removeObserver:self name:TVSNotificationName object:nil]

/*!
 * @brief 用于在 TVS 通知回调方法中取出 TVSAccountEvent（事件类型）
 * @param notify NSNotification 对象
 * @return TVSAccountEvent
 */
#define TVSNotificationGetEvent(notify) [[notify.userInfo valueForKey:TVSNotificationKey_EVENT]intValue]

/*!
 * @brief 用于在 TVS 通知回调方法中取出 SUCCESS（是否成功）
 * @param notify NSNotification 对象
 * @return SUCCESS
 */
#define TVSNotificationGetSuccess(notify) [[notify.userInfo valueForKey:TVSNotificationKey_SUCCESS]boolValue]



/*!
 * @typedef TVSEvent
 * @brief TVS 事件
 */
typedef NS_ENUM(NSUInteger, TVSAccountEvent) {
    /*!
     * @brief 获取微信 Token 事件
     */
    TVSAccountEventFetchWXToken,
    
    /*!
     * @brief 刷新微信 Token 事件
     */
    TVSAccountEventRefreshWXToken,
    
    /*!
     * @brief 验证 QQ token 事件
     */
    TVSAccountEventVerifyQQToken,
    
    /*!
     * @brief 获取 TVSID 事件
     */
    TVSAccountEventFetchTvsId,
    
    /*!
     * @brief 获取验证码事件
     */
    TVSAccountEventGetCaptcha,
    
    /*!
     * @brief 绑定手机号事件
     */
    TVSAccountEventBindPhoneNumber,
    
    /*!
     * @brief 绑定地址事件
     */
    TVSAccountEventBindLocation,
    
    /*!
     * @brief 查询地址事件
     */
    TVSAccountEventQueryLocation,
    
    /*!
     * @brief 绑定设备 push 信息事件
     */
    TVSAccountEventBindPushInfo
};



/*!
 * @class TVSAccountInfo
 * @brief 账号信息
 */
@interface TVSAccountInfo : NSObject

/*!
 * @brief 微信/QQ登录的 openID
 */
@property(nonatomic,copy) NSString* appId;

/*!
 * @brief 微信/QQ登录的 openID
 */
@property(nonatomic,copy) NSString* openId;

/*!
 * @brief 微信/QQ登录的 accessToken
 */
@property(nonatomic,copy) NSString* accessToken;

/*!
 * @brief 微信登录的 refreshToken
 */
@property(nonatomic,copy) NSString* refreshToken;

/*!
 * @brief  微信登录的 unionId
 */
@property(nonatomic,assign) NSString *unionId;

/*!
 * @brief 微信/QQ token 的过期时间
 */
@property(nonatomic,assign) NSInteger expireTime;

/*!
 * @brief TVS 平台返回的 tvsId
 */
@property(nonatomic,copy) NSString* tvsId;

/*!
 * @brief ClientId
 */
@property(nonatomic,copy) NSString* clientId;

/*!
 * @brief userid
 */
@property(nonatomic,copy) NSString* userId;

@end



/*!
 * @class TVSGeoLocation
 * @brief 位置信息
 */
@interface TVSGeoLocation : NSObject <NSCoding>

/*!
 * @brief 名称
 */
@property (nonatomic,copy) NSString* name;

/*!
 * @brief 地址
 */
@property (nonatomic,copy) NSString* addr;

/*!
 * @brief 经度
 */
@property (nonatomic,copy) NSString* lng;

/*!
 * @brief 纬度
 */
@property (nonatomic,copy) NSString* lat;

@end



/*!
 * @class TVSUserInfo
 * @brief 用户信息
 */
@interface TVSUserInfo : NSObject

/*!
 * @brief id类型（0：微信，1：QQ）
 */
@property(nonatomic,assign) NSInteger idType;

/*!
 * @brief 昵称
 */
@property(nonatomic,copy) NSString* nickName;

/*!
 * @brief 头像
 */
@property(nonatomic,copy) NSString* headImgUrl;

/*!
 * @brief 性别（0：女，1：男）
 */
@property(nonatomic,assign) NSInteger sex;

/*!
 * @brief 手机号
 */
@property(nonatomic,copy) NSString* phoneNumber;

/*!
 * @brief 家庭地址
 */
@property(nonatomic,strong) TVSGeoLocation*  homeLocation;

/*!
 * @brief 公司地址
 */
@property(nonatomic,strong) TVSGeoLocation* companyLocation;

@end



/*!
 * @class TVSAccountSDK
 * @brief TVS 账号 sdk
 */
@interface TVSAccountSDK : NSObject

/*!
 * @brief 是否测试环境
 */
@property (nonatomic,assign) BOOL testEnvironment;

/*!
 * @brief 获得 TVS 授权帮助类单例对象
 * @return TVS 授权帮助类实例
 */
+(instancetype)shared;

/*!
 * @brief 注册 App (自动从 Info.plist 读取相关参数)
 * @warning 必须在 AppDelegate 的 application:didFinishLaunchingWithOptions: 方法中调用
 */
-(void)registerApp;

/*!
 * @brief 处理 URL 跳转
 * @warning 必须在 AppDelegate 的 application:handleOpenURL: 和 application:openURL:sourceApplication:annotation: 两个方法中调用
 * @param url 待处理的 URL
 * @return 是否成功处理相关 URL 跳转
 */
-(BOOL)handleOpenUrl:(NSURL*)url;

/*!
 * @brief 检查微信 Token 是否存在
 * @return 是否存在
 */
-(BOOL)isWXTokenExist;

/*!
 * @brief 检查 QQ Token 是否存在
 * @return 是否存在
 */
-(BOOL)isQQTokenExist;

/*!
 * @brief 微信登录
 * @warning 如果微信 token 不存在，则必须调用此方法，以获得 TVS 后台返回的相关账户信息
 */
-(void)loginWithWX;

/*!
 * @brief QQ 登录
 * @warning 如果 QQ token 不存在，则必须调用此方法，以获得 TVS 后台返回的相关账户信息
 */
-(void)loginWithQQ;

/*!
 * @brief 刷新微信 Token
 * @warning 如果微信 token 存在，则必须调用此方法，以获得(更新) TVS 后台返回的相关账户信息
 */
-(void)refreshWXToken;

/*!
 * @brief 验证 QQ Token
 * @warning 如果 QQ token 存在，则必须调用此方法，以获得(更新) TVS 后台返回的相关账户信息
 */
-(void)verifyQQToken;

/*!
 * @brief 获取账号信息
 * @return accountInfo
 */
-(TVSAccountInfo*)accountInfo;

/*!
 * @brief 获取用户信息
 * @return userInfo
 */
-(TVSUserInfo*)userInfo;

/*!
 * @brief 注销登录
 */
-(void)logout;

/*!
 * @brief 打开用户中心页面
 * @warning 暂时只支持微信登录和微信支付
 * @param viewController 起始ViewController
 */
-(void)enterUserCenterPageFromViewController:(UIViewController*)viewController;

/*!
 * @brief 获取用于绑定手机号的验证码
 * @warning 必须确保已登录
 * @param phoneNumber 手机号
 */
-(void)getCaptchaWithPhoneNumber:(NSString*)phoneNumber;

/*!
 * @brief 绑定手机号
 * @warning 必须确保已登录
 * @param phoneNumber 手机号
 * @param captcha 验证码
 */
-(void)bindPhoneNumber:(NSString*)phoneNumber Captcha:(NSString*)captcha;

/*!
 * @brief 绑定地址
 * @warning 必须确保已登录
 * @param homeLoc 家庭地址
 * @param companyLoc 公司地址
 */
-(void)bindHomeLocation:(TVSGeoLocation*)homeLoc CompanyLocation:(TVSGeoLocation*)companyLoc;

/*!
 * @brief 查询地址
 * @warning 必须确保已登录
 */
-(void)queryLocation;

/*!
 * @brief 绑定用于 Push 的设备相关信息
 * @warning 必须确保已登录
 * @param guid 设备唯一标识
 * @param pushId
 * @param pushIdExtra
 * @param qua
 * @param imei 设备 IMEI
 * @param lc License
 * @param mac 设备 MAC 地址
 * @param qimei
 * @param enrollTime 设备注册 CT 时间
 * @param bindTime 设备绑定 CT 时间
 */
-(void)bindPushInfoWithGuid:(NSString*)guid PushId:(NSString*)pushId PushIdExtra:(NSString*)pushIdExtra Qua:(NSString*)qua IMEI:(NSString*)imei LC:(NSString*)lc MAC:(NSString*)mac QIMEI:(NSString*)qimei EnrollTime:(NSInteger)enrollTime BindTime:(NSInteger)bindTime;

@end
