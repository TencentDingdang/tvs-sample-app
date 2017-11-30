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
 * @class TVSWLANDevice
 * @brief 扫描到的无线局域网叮当设备
 */
@interface TVSWLANDevice : NSObject

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
 * @class TVSPushDevice
 * @brief TVS Push 设备
 */
@interface TVSPushDevice : NSObject

/*!
 * @brief pushId
 */
@property(nonatomic,copy) NSString* pushId;

/*!
 * @brief pushIdExtra
 */
@property(nonatomic,copy) NSString* pushIdExtra;

/*!
 * @brief guid
 */
@property(nonatomic,copy) NSString* guid;

/*!
 * @brief deviceId 设备ID
 */
@property(nonatomic,copy) NSString* deviceId;

/*!
 * @brief deviceName 设备名
 */
@property(nonatomic,copy) NSString* deviceName;

/*!
 * @brief deviceType 设备类型
 */
@property(nonatomic,copy) NSString* deviceType;

/*!
 * @brief deviceSerial 设备系列
 */
@property(nonatomic,copy) NSString* deviceSerial;

/*!
 * @brief deviceOEM 设备厂商
 */
@property(nonatomic,copy) NSString* deviceOEM;

/*!
 * @brief deviceOEMUrl 设备品牌图标
 */
@property(nonatomic,copy) NSString* deviceOEMUrl;

/*!
 * @brief deviceMark 设备备注
 */
@property(nonatomic,copy) NSString* deviceMark;

/*!
 * @brief QUA
 */
@property(nonatomic,copy) NSString* QUA;

/*!
 * @brief IMEI
 */
@property(nonatomic,copy) NSString* IMEI;

/*!
 * @brief LC
 */
@property(nonatomic,copy) NSString* LC;

/*!
 * @brief MAC
 */
@property(nonatomic,copy) NSString* MAC;

/*!
 * @brief QIMEI
 */
@property(nonatomic,copy) NSString* QIMEI;

/*!
 * @brief enrollTime 注册时间
 */
@property(nonatomic,assign) long long enrollTime;

/*!
 * @brief bindTime 绑定时间
 */
@property(nonatomic,assign) long long bindTime;

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
 * @param guid (APP或设备的)guid
 * @param pushId (APP或设备的)pushId
 * @param pushIdExtra (APP或设备的)pushIdExtra
 * @param pushDevice push 设备信息
 * @param handler 回调，BOOL 值表示是否成功
 */
-(void)bindPushInfoWithGuid:(NSString*)guid pushId:(NSString*)pushId pushIdExtra:(NSString*)pushIdExtra pushDevice:(TVSPushDevice*)pushDevice handler:(void(^)(BOOL))handler;

/*!
 * @brief 解除绑定 Push 相关信息
 * @warning 必须确保已登录
 * @param guid
 * @param pushId
 * @param pushIdExtra
 * @param pushDevice push设备
 * @param handler 回调，BOOL 值表示是否成功
 */
-(void)unbindPushInfoWithGuid:(NSString*)guid pushId:(NSString*)pushId pushIdExtra:(NSString*)pushIdExtra pushDevice:(TVSPushDevice*)pushDevice handler:(void(^)(BOOL))handler;

/*!
 * @brief 查询绑定过的 push 设备列表
 * @warning 必须确保已登录
 * @param handler 回调
 */
-(void)queryPushDevicesWithHandler:(void(^)(NSArray<TVSPushDevice*>*))handler;

/*!
 * @brief 扫描当前无线局域网内(集成了叮当语音服务)的设备(音箱、电视、耳机等)
 * @param handler 回调
 */
-(void)discoverWlanDevicesWithHandler:(void(^)(TVSWLANDevice*))handler;

/*!
 * @brief 将 WiFi 信息同步给待配网的叮当设备
 * @warning 必须确保 APP 已接入设备所在热点 WiFi
 * @param ssid WiFi 唯一标识
 * @param password WiFi 密码
 * @param ip 设备热点 WiFi 网关地址
 * @param handler 回调
 */
-(void)sendWifiSsid:(NSString*)ssid password:(NSString*)password ip:(NSString*)ip handler:(void(^)(TVSWLANDevice*))handler;

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
