//
//  TVSAuth.h
//  TVSAccountSDK
//
//  Created by RincLiu on 21/08/2017.
//  Copyright © 2017 tencent. All rights reserved.
//

/*!
 * @file TVSAuth.h
 */
#import <Foundation/Foundation.h>

/*!
 * @brief 非法的 ClientId 常量
 */
static NSString* const INVALID_CLIENT_ID = @"invalid clientId";

/*!
 * @brief 非法的 RefreshToken 常量
 */
static NSString* const INVALID_REFRESH_TOKEN = @"refreshToken";



/*!
 * @typedef TVSAuthEvent
 * @brief TVS 授权回调事件
 */
typedef NS_ENUM(NSUInteger, TVSAuthEvent) {
    /*!
     * @brief 获取微信 Token
     */
    TVSAuthEventFetchWXToken,
    
    /*!
     * @brief 刷新微信 Token
     */
    TVSAuthEventRefreshWXToken,
    
    /*!
     * @brief 验证 QQ token
     */
    TVSAuthEventVerifyQQToken,
    
    /*!
     * @brief 获取 TVSID
     */
    TVSAuthEventFetchId
};



/*!
 * @protocol TVSAuthDelegate
 * @brief TVS 授权回调协议
 * @warning 必须实现本协议，否则无法收到相关事件回调
 */
@protocol TVSAuthDelegate <NSObject>

@required
/*!
 * @brief 事件回调
 * @param event 事件类型
 * @param success 是否成功
 */
-(void)TVSAuthEvent:(TVSAuthEvent)event Success:(BOOL)success;

@end



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

@end



/*!
 * @class TVSAuth
 * @brief TVS 授权 API
 */
@interface TVSAuth : NSObject

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
 * @brief 设置 TVS 授权回调
 * @param authDelegate TVSAuthDelegate
 * @warning 如果要接收授权回调事件，必须实现此协议
 */
-(void)setAuthDelegate:(id<TVSAuthDelegate>)authDelegate;

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

@end
