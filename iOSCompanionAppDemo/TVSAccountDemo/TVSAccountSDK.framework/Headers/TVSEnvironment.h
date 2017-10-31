//
//  TVSEnvironment.h
//  TVSAccountSDK
//
//  Created by Rinc Liu on 30/10/2017.
//  Copyright © 2017 tencent. All rights reserved.
//

/*!
 * @file TVSEnvironment.h
 */
#import <Foundation/Foundation.h>

/*!
 * @class TVSEnvironment
 * @brief TVS 环境配置 API
 */
@interface TVSEnvironment : NSObject

/*!
 * @brief 获得实例
 * @return 实例
 */
+(instancetype)shared;

/*!
 * @brief 是否测试环境
 */
@property (nonatomic,assign) BOOL test;

@end
