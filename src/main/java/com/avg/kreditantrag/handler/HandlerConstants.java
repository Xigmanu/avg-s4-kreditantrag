package com.avg.kreditantrag.handler;

public class HandlerConstants {
    public static final String MESSAGE_CORRELATION_KEY = "empId";
    /*-----------------------------------PROCESS_TERMINATION-----------------------------------*/
    public static final String TIMEOUT_TERMINATION_MESSAGE = "TimeoutTerminationMessage";
    public static final String MANUAL_APPROVED_TERMINATION_MESSAGE = "ManualApprovedTerminationMessage";
    public static final String MANUAL_REJECTED_TERMINATION_MESSAGE = "ManualRejectedTerminationMessage";
    public static final String TERMINATE_MAIL_SERVICE_MESSAGE = "TerminateMailServiceMessage";

    /*-----------------------------------EMAILS-----------------------------------*/
    public static final String VERIFICATION_EMAIL_MESSAGE = "VerificationEmailMessage";
    public static final String EMAIL_TO_MANAGER_MESSAGE = "EmailToManagerMessage";
    public static final String APPROVAL_EMAIL_MESSAGE = "ApprovalEmailMessage";
    public static final String REJECTION_EMAIL_MESSAGE = "RejectionEmailMessage";
    public static final String IN_PROCESS_EMAIL_MESSAGE = "InProcessEmailMessage";
    public static final String TIMEOUT_EMAIL_MESSAGE = "TimeoutEmailMessage";
    public static final String CONFIRMATION_EMAIL_MESSAGE = "ConfirmationEmailMessage";
    public static final String AUTO_APPROVAL_EMAIL_MESSAGE = "AutoApprovalEmailMessage";

    /*-----------------------------------NOTIFICATION-----------------------------------*/
    public static final String NOTIFY_THE_MANAGER_MESSAGE = "NotifyTheManagerMessage"; // notify-manager-message
}
