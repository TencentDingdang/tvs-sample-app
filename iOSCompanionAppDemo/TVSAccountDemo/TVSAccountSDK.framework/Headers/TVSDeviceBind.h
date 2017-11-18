//
//  TVSDeviceBind.h
//  TVSAccountSDK
//
//  Created by Rinc Liu on 17/11/2017.
//  Copyright © 2017 tencent. All rights reserved.
//

#import <Foundation/Foundation.h>


/*!
 * @brief 设备绑定结果
 */
typedef NS_ENUM(NSInteger,TVSBindDeviceResult) {
    /*!
     * @brief 设备绑定结果：成功
     */
    TVSBindDeviceResultSuccess,
    
    /*!
     * @brief 设备绑定结果：已被绑定
     */
    TVSBindDeviceResultHasBinded,
    
    /*!
     * @brief 设备绑定结果：参数格式错误
     */
    TVSBindDeviceResultParamsInvalid
};



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
 * @class TVSDeviceBind
 * @brief TVS 设备发现/绑定接口
 */
@interface TVSDeviceBind : NSObject

/*!
 * @brief 获得 TVS 设备发现类单例对象
 * @return TVS 设备发现类实例
 */
+(instancetype)shared;

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
 * @param handler 回调，BOOL 值表示是否成功
 */
-(void)bindPushInfoWithGuid:(NSString*)guid pushId:(NSString*)pushId pushIdExtra:(NSString*)pushIdExtra qua:(NSString*)qua imei:(NSString*)imei lc:(NSString*)lc mac:(NSString*)mac qimei:(NSString*)qimei isApp:(BOOL)isApp handler:(void(^)(BOOL))handler;

/*!
 * @brief 扫描当前无线局域网内(集成了叮当语音服务)的设备(音箱、电视、耳机等)
 * @param handler 回调
 */
-(void)discoverWlanDevicesWithHandler:(void(^)(TVSDevice*))handler;

/*!
 * @brief 将 WiFi 信息同步给待配网的叮当设备
 * @warning 必须确保 APP 已接入设备所在热点 WiFi
 * @param ssid WiFi 唯一标识
 * @param password WiFi 密码
 * @param ip 设备热点 WiFi 网关地址
 * @param handler 回调
 */
-(void)sendWifiSsid:(NSString*)ssid password:(NSString*)password ip:(NSString*)ip handler:(void(^)(TVSDevice*))handler;

/*!
 * @brief (APP绑定设备成功后)将账号信息同步给设备
 * @warning 必须确保APP已登录
 * @param deviceIp 扫描到的局域网设备IP地址
 * @param bundleId App bundleId
 * @param guid app guid
 * @param handler 回调，参数为 TVSBindDeviceResult 枚举
 */
-(void)sendAccountInfoToDeviceIp:(NSString*)deviceIp bundleId:(NSString*)bundleId guid:(NSString*)guid handler:(void(^)(TVSBindDeviceResult))handler;

@end
