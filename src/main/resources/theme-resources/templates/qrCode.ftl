<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "title">
        ${msg("loginTitle",realm.name)}
    <#elseif section = "header">
        ${msg("loginTitleHtml",realm.name)}
    <#elseif section = "form">
        <form id="loginform" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <div class="${properties.kcFormGroupClass!}">
                <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                    <div class="${properties.kcFormOptionsWrapperClass!}"/>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <div class="${properties.kcFormButtonsWrapperClass!}">
                        <div class="camera-canvas" id="camera-canvas"></div>
                        <div class="loading-message" id="loading-message"></div>
                        <input name="qrCodeData" id="qrCodeData" type="hidden" value=""/>
                        <input name="login" id="kc-login" type="hidden" value="${msg("doLogIn")}"/>
                        <script>
                            var video = document.createElement("video");
                            var canvasElement = document.getElementById("camera-canvas");
                            var canvas = canvasElement.getContext("2d");
                            var loadingMessage = document.getElementById("loading-message");

                            function drawLine(begin, end, color) {
                                canvas.beginPath();
                                canvas.moveTo(begin.x, begin.y);
                                canvas.lineTo(end.x, end.y);
                                canvas.lineWidth = 4;
                                canvas.strokeStyle = color;
                                canvas.stroke();
                            }

                            navigator.mediaDevices.getUserMedia({ video: { facingMode: "environment" } }).then(function(stream) {
                                video.srcObject = stream;
                                video.setAttribute("playsinline", true); // required to tell iOS safari we don't want fullscreen
                                video.play();
                                requestAnimationFrame(tick);
                            });

                            function tick() {
                                loadingMessage.innerText = "Loading video..."
                                if (video.readyState === video.HAVE_ENOUGH_DATA) {
                                    loadingMessage.hidden = true;
                                    canvasElement.hidden = false;

                                    canvasElement.height = video.videoHeight;
                                    canvasElement.width = video.videoWidth;
                                    canvas.drawImage(video, 0, 0, canvasElement.width, canvasElement.height);
                                    var imageData = canvas.getImageData(0, 0, canvasElement.width, canvasElement.height);
                                    var code = jsQR(imageData.data, imageData.width, imageData.height, {
                                        inversionAttempts: "dontInvert",
                                    });
                                    if (code) {
                                        drawLine(code.location.topLeftCorner, code.location.topRightCorner, "#FF3B58");
                                        drawLine(code.location.topRightCorner, code.location.bottomRightCorner, "#FF3B58");
                                        drawLine(code.location.bottomRightCorner, code.location.bottomLeftCorner, "#FF3B58");
                                        drawLine(code.location.bottomLeftCorner, code.location.topLeftCorner, "#FF3B58");

                                        document.getElementById('qrCodeData').value = code.data;
                                        document.getElementById('loginForm').submit();
                                    } else {
                                        //no data
                                        requestAnimationFrame(tick);
                                    }
                                }
                            }
                        </script>
                        <script src="https://cdn.jsdelivr.net/npm/jsqr@1.3.1/dist/jsQR.js" integrity="sha256-WZjYWhu0ZfavKsNtUWMFYSibq3Tz8vDMaZ9NwTMsA0U=" crossorigin="anonymous"></script>
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}"
                               name="cancel" id="kc-cancel" type="submit" value="${msg("doCancel")}"/>
                    </div>
                </div>
            </div>
        </form>
    </#if>
</@layout.registrationLayout>