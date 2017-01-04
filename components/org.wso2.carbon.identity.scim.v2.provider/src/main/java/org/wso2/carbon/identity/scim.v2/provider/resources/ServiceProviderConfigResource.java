/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


package org.wso2.carbon.identity.scim.v2.provider.resources;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.scim.v2.common.impl.IdentitySCIMManager;
import org.wso2.carbon.identity.scim.v2.provider.util.SCIMProviderConstants;
import org.wso2.carbon.identity.scim.v2.provider.util.SupportUtils;
import org.wso2.charon3.core.encoder.JSONEncoder;
import org.wso2.charon3.core.exceptions.CharonException;
import org.wso2.charon3.core.extensions.UserManager;
import org.wso2.charon3.core.protocol.SCIMResponse;
import org.wso2.charon3.core.protocol.endpoints.ServiceProviderConfigResourceManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class ServiceProviderConfigResource extends AbstractResource {
    private static Log logger = LogFactory.getLog(ServiceProviderConfigResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser() {

        String userName = SCIMProviderConstants.DEFAULT_USERNAME;
        JSONEncoder encoder = null;
        try {

            IdentitySCIMManager identitySCIMManager = IdentitySCIMManager.getInstance();

            // obtain the encoder at this layer in case exceptions needs to be encoded.
            encoder = identitySCIMManager.getEncoder();

            // obtain the user store manager
            UserManager userManager = IdentitySCIMManager.getInstance().getUserManager(userName);

            // create charon-SCIM service provider config endpoint and hand-over the request.
            ServiceProviderConfigResourceManager serviceProviderConfigResourceManager = new ServiceProviderConfigResourceManager();

            SCIMResponse scimResponse = serviceProviderConfigResourceManager.get(null, null, null, null);
            // needs to check the code of the response and return 200 0k or other error codes
            // appropriately.
            return new SupportUtils().buildResponse(scimResponse);

        } catch (CharonException e) {
            return handleCharonException(e,encoder);
        }
    }
}