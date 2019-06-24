/*
 * Copyright 2019 ThoughtWorks, Inc.
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

package com.thoughtworks.go.apiv2.environments;

import com.thoughtworks.go.api.ApiController;
import com.thoughtworks.go.api.ApiVersion;
import com.thoughtworks.go.api.spring.ApiAuthenticationHelper;
import com.thoughtworks.go.api.util.HaltApiResponses;
import com.thoughtworks.go.spark.Routes;
import com.thoughtworks.go.spark.spring.SparkSpringController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static spark.Spark.before;
import static spark.Spark.path;

@Component
public class EnvironmentsControllerV2 extends ApiController implements SparkSpringController {
    private final ApiAuthenticationHelper apiAuthenticationHelper;

    private static final String unsupportedAPIName = "Environments";

    @Autowired
    public EnvironmentsControllerV2(ApiAuthenticationHelper apiAuthenticationHelper) {
        super(ApiVersion.v2);
        this.apiAuthenticationHelper = apiAuthenticationHelper;
    }

    @Override
    public String controllerBasePath() {
        return Routes.Environments.BASE;
    }

    @Override
    public void setupRoutes() {
        path(controllerBasePath(), () -> {
            before("", mimeType, this::setContentType);
            before("/*", mimeType, this::setContentType);

            before("", mimeType, apiAuthenticationHelper::checkAdminUserAnd403);
            before("/*", mimeType, apiAuthenticationHelper::checkAdminUserAnd403);

            before("", mimeType, (req, resp) -> {
                throw HaltApiResponses.haltBecauseOfUnsupportedAPIVersion(mimeType, unsupportedAPIName);
            });
            before("/*", mimeType, (req, resp) -> {
                throw HaltApiResponses.haltBecauseOfUnsupportedAPIVersion(mimeType, unsupportedAPIName);
            });
        });
    }
}
