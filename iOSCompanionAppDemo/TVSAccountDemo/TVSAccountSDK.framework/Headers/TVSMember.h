//
//  TVSMember.h
//  TVSAccountSDK
//
//  Created by Rinc Liu on 17/11/2017.
//  Copyright © 2017 tencent. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

/*!
 * @brief TVS 会员类型
 */
typedef NS_ENUM(NSInteger,TVSMemberType) {
    /*!
     * @brief QQ 音乐会员
     */
    TVSMemberTypeQQMusic
};



/*!
 * @brief TVS 相关 H5 页面
 */
typedef NS_ENUM(NSInteger,TVSPageType) {
    /*!
     * @brief 领取会员页面
     */
    TVSPageTypeMember,
    /*!
     * @brief 手机号地址页面
     */
    TVSPageTypePhoneAddress,
    /*!
     * @brief 用户反馈页面
     */
    TVSPageTypeFeedback
};



/*!
 * @protocol TVSPageDelegate
 * @brief TVS ViewControler 回调
 */
@protocol TVSPageDelegate<NSObject>

@optional
/*!
 * @brief H5页面已加载
 * @param pageType 页面类型
 */
-(void)TVSDidLoadPage:(TVSPageType)pageType;

@optional
/*!
 * @brief H5页面即将出现
 * @param pageType 页面类型
 */
-(void)TVSWillAppearPage:(TVSPageType)pageType;

@optional
/*!
 * @brief H5页面已经出现
 * @param pageType 页面类型
 */
-(void)TVSDidAppearPage:(TVSPageType)pageType;

@optional
/*!
 * @brief H5页面即将消失
 * @param pageType 页面类型
 */
-(void)TVSWillDisappearPage:(TVSPageType)pageType;

@optional
/*!
 * @brief H5页面已经消失
 * @param pageType 页面类型
 */
-(void)TVSDidDisappearPage:(TVSPageType)pageType;

@optional
/*!
 * @brief H5页面QQ登录
 * @param pageType 页面类型
 * @param result QQ登录结果
 */
-(void)TVSPage:(TVSPageType)pageType qqLoginResult:(BOOL)result;

@optional
/*!
 * @brief H5页面微信登录
 * @param pageType 页面类型
 * @param result 微信登录结果
 */
-(void)TVSPage:(TVSPageType)pageType wxLoginResult:(BOOL)result;

@optional
/*!
 * @brief H5页面QQ验票
 * @param pageType 页面类型
 * @param result QQ验票结果
 */
-(void)TVSPage:(TVSPageType)pageType verifyQQTokenResult:(BOOL)result;

@optional
/*!
 * @brief H5页面微信刷票
 * @param pageType 页面类型
 * @param result 微信刷票结果
 */
-(void)TVSPage:(TVSPageType)pageType refreshWXTokenResult:(BOOL)result;

@optional
/*!
 * @brief H5页面微信支付
 * @param pageType 页面类型
 * @param result 微信支付结果
 */
-(void)TVSPage:(TVSPageType)pageType wxPayResult:(BOOL)result;

@optional
/*!
 * @brief H5页面关闭叮当智能语音服务
 * @param pageType 页面类型
 */
-(void)TVSUnbindAtPage:(TVSPageType)pageType;

@optional
/*!
 * @brief H5页面收到远程控制事件
 * @param pageType 页面类型
 * @param event 远程控制事件
 */
-(void)TVSPage:(TVSPageType)pageType receivedRemoteControlEvent:(UIEvent*)event;

@end



/*!
 * @class TVSMember
 * @brief TVS 会员相关接口
 */
@interface TVSMember: NSObject

/*!
 * @brief 设置设备相关信息（用于领取会员）
 * @param deviceType 设备类型
 * @param deviceOEM 设备厂商
 * @param productId TVS 后台申请的产品 id
 * @param dsn 设备号
 */
-(void)setDeviceType:(NSString*)deviceType deviceOEM:(NSString*)deviceOEM productId:(NSString*)productId DSN:(NSString*)dsn;

/*!
 * @brief 打开会员相关 H5 页面
 * @warning 暂时只支持微信支付
 * @param pageType H5 页面类型
 * @param fromViewController 起始页面
 * @param title 标题，如果传空会自动读取网页标题
 * @param delegate 页面回调
 * @return 是否成功
 */
-(BOOL)enterPage:(TVSPageType)pageType fromViewController:(UIViewController*)fromViewController title:(NSString*)title delegate:(id<TVSPageDelegate>)delegate;

/*!
 * @brief 查询会员状态
 * @warning 必须确保已登录
 * @param type 会员类型
 * @param handler 回调，两个 BOOL 值分别表示是否参与过活动、是否会员，两个 NSDate 分别表示会员开始时间、会员过期时间
 */
-(void)queryStatusWithType:(TVSMemberType)type handler:(void(^)(BOOL,BOOL,NSDate*,NSDate*))handler;

@end
