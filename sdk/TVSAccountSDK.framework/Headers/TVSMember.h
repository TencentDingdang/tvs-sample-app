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
-(void)TVSDidLoadPage:(TVSPageType)pageType;

@optional
-(void)TVSWillAppearPage:(TVSPageType)pageType;

@optional
-(void)TVSDidAppearPage:(TVSPageType)pageType;

@optional
-(void)TVSWillDisappearPage:(TVSPageType)pageType;

@optional
-(void)TVSDidDisappearPage:(TVSPageType)pageType;

@optional
-(void)TVSPage:(TVSPageType)pageType receivedRemoteControlEvent:(UIEvent*)event;

@end



/*!
 * @class TVSMember
 * @brief TVS 会员相关接口
 */
@interface TVSMember: NSObject

/*!
 * @brief 设置设备相关信息（用于领取会员）
 * @param deviceId 设备id
 * @param deviceType 设备类型
 * @param deviceOEM 设备厂商
 */
-(void)setDeviceId:(NSString*)deviceId deviceType:(NSString*)deviceType deviceOEM:(NSString*)deviceOEM;

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
