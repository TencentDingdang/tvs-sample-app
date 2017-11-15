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

/*!
 * @brief (打车点)名称
 */
@property (nonatomic,copy) NSString* cabName;

/*!
 * @brief (打车点)地址
 */
@property (nonatomic,copy) NSString* cabAddr;

/*!
 * @brief (打车点)经度
 */
@property (nonatomic,copy) NSString* cabLng;

/*!
 * @brief (打车点)纬度
 */
@property (nonatomic,copy) NSString* cabLat;

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
 * @class TVSDevice
 * @brief 集成了叮当语音服务的设备
 */
@interface TVSDevice : NSObject

/*!
 * @brief 设备(无线局域网)IP 地址，用于后续设备绑定
 */
@property(nonatomic,copy) NSString* deviceWlanIP;

/*!
 * @brief 设备名
 */
@property(nonatomic,copy) NSString* deviceName;

/*!
 * @brief 厂商
 */
@property(nonatomic,copy) NSString* manufacturer;

/*!
 * @brief 品牌图标
 */
@property(nonatomic,copy) NSString* brandIconUrl;

/*!
 * @brief 设备号
 */
@property(nonatomic,copy) NSString* DSN;

/*!
 * @brief MAC地址
 */
@property(nonatomic,copy) NSString* MAC;

/*!
 * @brief openId
 */
@property(nonatomic,copy) NSString* openId;

/*!
 * @brief 包名
 */
@property(nonatomic,copy) NSString* package;

/*!
 * @brief 操作系统
 */
@property(nonatomic,copy) NSString* OS;

/*!
 * @brief 全局唯一标识
 */
@property(nonatomic,copy) NSString* guid;

/*!
 * @brief 设备类型
 */
@property(nonatomic,assign) NSInteger type;

/*!
 * @brief 是否已绑定叮当APP
 */
@property(nonatomic,assign) BOOL isbinded;

@end



/*!
 * @class TVSAccountSDK
 * @brief TVS 账号 sdk
 */
@interface TVSAccountSDK : NSObject

/*!
 * @brief 是否测试环境
 */
@property(nonatomic,assign) BOOL testEnvironment;

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
 * @param handler 回调
 */
-(void)wxLoginWithHandler:(void(^)(BOOL))handler;

/*!
 * @brief 微信支付
 * @warning 注意后台生成的订单类型必须是app支付，不能是h5订单，否则微信会报错“支付场景非法”
 * @param partnerid (后台生成的微信支付订单)partnerid
 * @param prepayid (后台生成的微信支付订单)prepayid
 * @param package (后台生成的微信支付订单)package
 * @param noncestr (后台生成的微信支付订单)noncestr
 * @param sign (后台生成的微信支付订单)sign
 * @param timestamp (后台生成的微信支付订单)timestamp
 * @param handler 回调
 */
-(void)wxPayWithPartnerid:(NSString*)partnerid prepayid:(NSString*)prepayid package:(NSString*)package noncestr:(NSString*)noncestr sign:(NSString*)sign timestamp:(UInt32)timestamp handler:(void(^)(BOOL,NSString*))handler;

/*!
 * @brief QQ 登录
 * @warning 如果 QQ token 不存在，则必须调用此方法，以获得 TVS 后台返回的相关账户信息
 * @param handler 回调
 */
-(void)qqLoginWithHandler:(void(^)(BOOL))handler;

/*!
 * @brief 刷新微信 Token
 * @warning 如果微信 token 存在，则必须调用此方法，以获得(更新) TVS 后台返回的相关账户信息
 * @param handler 回调
 */
-(void)wxTokenRefreshWithHandler:(void(^)(BOOL))handler;

/*!
 * @brief 验证 QQ Token
 * @warning 如果 QQ token 存在，则必须调用此方法，以获得(更新) TVS 后台返回的相关账户信息
 * @param handler 回调
 */
-(void)qqTokenVerifyWithHandler:(void(^)(BOOL))handler;

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
 * @param handler 回调
 */
-(void)getCaptchaWithPhoneNumber:(NSString*)phoneNumber handler:(void(^)(BOOL))handler;

/*!
 * @brief 绑定手机号
 * @warning 必须确保已登录
 * @param phoneNumber 手机号
 * @param captcha 验证码
 * @param handler 回调
 */
-(void)bindPhoneNumber:(NSString*)phoneNumber captcha:(NSString*)captcha handler:(void(^)(BOOL))handler;

/*!
 * @brief 绑定地址
 * @warning 必须确保已登录
 * @param homeLoc 家庭地址
 * @param companyLoc 公司地址
 * @param handler 回调
 */
-(void)bindHomeLocation:(TVSGeoLocation*)homeLoc companyLocation:(TVSGeoLocation*)companyLoc handler:(void(^)(BOOL))handler;

/*!
 * @brief 查询地址
 * @warning 必须确保已登录
 * @param handler 回调
 */
-(void)queryLocationWithHandler:(void(^)(NSArray*,NSArray*))handler;

/*!
 * @brief 绑定 Push 相关信息
 * @warning 必须确保已登录
 * @param guid 
 * @param pushId
 * @param pushIdExtra
 * @param qua 设备QUA(APP可传nil)
 * @param imei 设备 IMEI(APP可传nil)
 * @param lc 设备License(APP可传nil)
 * @param mac 设备 MAC 地址(APP可传nil)
 * @param qimei 设备QIMEI(APP可传nil)
 * @param isApp 是否App
 * @param handler 回调
 */
-(void)bindPushInfoWithGuid:(NSString*)guid pushId:(NSString*)pushId pushIdExtra:(NSString*)pushIdExtra qua:(NSString*)qua imei:(NSString*)imei lc:(NSString*)lc mac:(NSString*)mac qimei:(NSString*)qimei isApp:(BOOL)isApp handler:(void(^)(BOOL))handler;

/*!
 * @brief 扫描当前无线局域网内(集成了叮当语音服务)的设备(音箱、电视、耳机等)
 * @param handler 回调
 */
-(void)discoverWlanDevicesWithHandler:(void(^)(TVSDevice*))handler;

/*!
 * @brief (APP绑定设备成功后)将账号信息同步给设备
 * @warning 必须确保APP已登录
 * @param deviceIp 扫描到的局域网设备IP地址
 * @param bundleId App bundleId
 * @param guid app guid
 *@param handler 回调
 */
-(void)sendAccountInfoToDeviceIp:(NSString*)deviceIp bundleId:(NSString*)bundleId guid:(NSString*)guid handler:(void(^)(BOOL))handler;

@end

