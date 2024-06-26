/**
 * Copyright © 2016-2022 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.elco.eeds.agent.sdk.core.script;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.script.domain.Virtual;

import java.util.List;

public interface JsInvokeService {

    void eval( String scriptBody, String... argNames);

    String execute(Virtual virtual, List<PropertiesValue> propertiesValueList);


//    ListenableFuture<Object> invokeFunction(TenantId tenantId, CustomerId customerId, UUID scriptId, Object... args);
//
//    ListenableFuture<Void> release(UUID scriptId);

}
