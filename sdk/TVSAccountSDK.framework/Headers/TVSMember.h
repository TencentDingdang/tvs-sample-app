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
 * @class TVSMember
 * @brief TVS 会员相关接口
 */
@interface TVSMember: NSObject

/*!
 * @brief 获取 TVSMember 实例
 * @param deviceId 设备 ID
 * @param deviceType 设备类型
 * @param deviceOEM 设备厂商
 * @return TVSMember 实例
 */
-(instancetype)initWithDeviceId:(NSString*)deviceId deviceType:(NSString*)deviceType deviceOEM:(NSString*)deviceOEM;

/*!
 * @brief 打开会员页面
 * @warning 暂时只支持微信支付
 * @param viewController 起始ViewController
 * @return 如果失败，请检查 deviceId/deviceType/DeviceOEM 和 viewController 是否为空
 */
-(BOOL)enterPageFromViewController:(UIViewController*)viewController;

/*!
 * @brief 查询会员状态
 * @warning 必须确保已登录
 * @param type 会员类型
 * @param handler 回调，两个 BOOL 值分别表示是否参与过活动、是否会员，两个 NSDate 分别表示会员开始时间、会员过期时间
 */
-(void)queryStatusWithType:(TVSMemberType)type handler:(void(^)(BOOL,BOOL,NSDate*,NSDate*))handler;

@end
