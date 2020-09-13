<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "title">
    <#elseif section = "header">
    <#elseif section = "form">
        <form id="loginform" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <div class="${properties.kcFormGroupClass!}">
                <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                    <div class="${properties.kcFormOptionsWrapperClass!}"/>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <div class="${properties.kcFormButtonsWrapperClass!}">
                        <center>
                        <div class="video-blocked" id="video-blocked">
                            <i class="fas fa-video" style="font-size: 24px; color: red; text-align: center;"></i><br />
                            <h2>Video Permission Required</h2>
                            <p>Please allow us to access the camera in order to scan your login code!</p>
                        </div>
                        <div class="video-loading" id="video-loading" hidden>
                            <i class="fas fa-hourglass" style="font-size: 24px; color: blue; text-align: center;"></i><br />
                            <h2>Loading Video</h2>
                            <p>Please hold tight, the camera is starting!</p>
                        </div>
                        <div class="video-scan" id="video-scan" hidden>
                            <i class="fas fa-qrcode" style="font-size: 24px; color: blue; text-align: center;"></i><br />
                            <p>Align the QR code in the view finder to log in</p><br />
                            <canvas class="camera-canvas" id="camera-canvas" style="width: 100%; height: auto; margin-bottom: 20px;" hidden></canvas>
                        </div>
                        <div class="scan-complete" id="scan-complete" hidden>
                             <i class="fas fa-check" style="font-size: 24px; color: green; text-align: center;"></i><br />
                             <h2>Logging you in...</h2>
                             <br />
                        </div>
                        </center>

                        <input name="qrCodeData" id="qrCodeData" type="hidden" value=""/>
                        <input name="login" id="kc-login" type="hidden" value="${msg("doLogIn")}"/>

                        <script>
                            var video = document.createElement("video");
                            var canvasElement = document.getElementById("camera-canvas");
                            var canvas = canvasElement.getContext("2d");

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
                                document.getElementById('video-blocked').hidden = true;
                                document.getElementById('video-loading').hidden = false;
                                requestAnimationFrame(tick);
                            });

                            function tick() {
                                if (video.readyState === video.HAVE_ENOUGH_DATA) {
                                    document.getElementById('video-loading').hidden = true;
                                    document.getElementById('video-scan').hidden = false;
                                    canvasElement.hidden = false;

                                    canvasElement.height = video.videoHeight;
                                    canvasElement.width = video.videoWidth;
                                    canvas.drawImage(video, 0, 0, canvasElement.width, canvasElement.height);
                                    var imageData = canvas.getImageData(0, 0, canvasElement.width, canvasElement.height);
                                    var code = jsQR(imageData.data, imageData.width, imageData.height, {
                                        inversionAttempts: "dontInvert",
                                    });
                                    if (code) {
                                        drawLine(code.location.topLeftCorner, code.location.topRightCorner, "#3BFF80");
                                        drawLine(code.location.topRightCorner, code.location.bottomRightCorner, "#3BFF80");
                                        drawLine(code.location.bottomRightCorner, code.location.bottomLeftCorner, "#3BFF80");
                                        drawLine(code.location.bottomLeftCorner, code.location.topLeftCorner, "#3BFF80");

                                        document.getElementById('qrCodeData').value = code.data;
                                        setTimeout(() => {
                                            document.getElementById('video-scan').hidden = true;
                                            document.getElementById('scan-complete').hidden = false;
                                            document.getElementById('loginform').submit();
                                        }, 1000);

                                        return;
                                    } else {
                                        //no data
                                    }
                                } else {
                                    document.getElementById('video-loading').hidden = false;
                                    document.getElementById('video-scan').hidden = true;
                                }
                                requestAnimationFrame(tick);
                            }
                        </script>
                        <script src="https://cdn.jsdelivr.net/npm/jsqr@1.3.1/dist/jsQR.js" integrity="sha256-WZjYWhu0ZfavKsNtUWMFYSibq3Tz8vDMaZ9NwTMsA0U=" crossorigin="anonymous"></script>
                        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.14.0/js/all.min.js" integrity="sha512-YSdqvJoZr83hj76AIVdOcvLWYMWzy6sJyIMic2aQz5kh2bPTd9dzY3NtdeEAzPp/PhgZqr4aJObB3ym/vsItMg==" crossorigin="anonymous"></script>
                        <!--<br />
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}"
                               name="cancel" id="kc-cancel" type="submit" value="${msg("doCancel")}"/>-->
                    </div>
                </div>
            </div>
        </form>
    </#if>
</@layout.registrationLayout>