/**
 * Copyright (c) 2015-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#import <Foundation/Foundation.h>

#import <ReactABI27_0_0/ABI27_0_0RCTDefines.h>

@protocol ABI27_0_0RCTReloadListener
- (void)didReceiveReloadCommand;
@end

/** Registers a weakly-held observer of the Command+R reload key command. */
ABI27_0_0RCT_EXTERN void ABI27_0_0RCTRegisterReloadCommandListener(id<ABI27_0_0RCTReloadListener> listener);

/** Triggers a reload for all current listeners. You shouldn't need to use this directly in most cases. */
ABI27_0_0RCT_EXTERN void ABI27_0_0RCTTriggerReloadCommandListeners(void);
