<?php
// Details for your email server and email to send api results to ("sentToEmail")
return array(
    'host' => 'mail.your-email-host.com',
    'SMTPAuth' => true,
    'SMTPSecure' => 'tls',
    'port' => 587,
    'username' => 'sender@email.com',
    'password' => 'password',
    'fromAddress' => 'sender@email.com',
    'replyAddress' => 'sender@email.com',
    'sendToEmail' => 'recipient_email@email.com'
);
