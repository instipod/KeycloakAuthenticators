<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "title">
    Select an authentication method
    <#elseif section = "header">
    Select an authentication method
    <#elseif section = "form">
        <form id="loginform" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <div class="${properties.kcFormGroupClass!}">
                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <div class="${properties.kcFormButtonsWrapperClass!}">
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" name="choice" id="kc-cancel" type="submit" value="Sign in using QR Code"/><br /><br />
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" name="choice" id="kc-cancel" type="submit" value="Sign in using Password"/>
                    </div>
                </div>
            </div>
        </form>
    </#if>
</@layout.registrationLayout>
