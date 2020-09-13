<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "title">
    Login Denied
    <#elseif section = "header">
    Login Denied
    <#elseif section = "form">
        <form id="loginform" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <div class="${properties.kcFormGroupClass!}">
                <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                    <div class="${properties.kcFormOptionsWrapperClass!}"/>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <div class="${properties.kcFormButtonsWrapperClass!}">
                    </div>
                </div>
            </div>
        </form>
    </#if>
</@layout.registrationLayout>