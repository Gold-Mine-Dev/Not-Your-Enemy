<?php
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require './php-mailer/Exception.php';
require './php-mailer/PHPMailer.php';
require './php-mailer/SMTP.php';

$config = include('config.php');

if (!isset($_POST['description']) || !isset($_POST['interviewee']) || !isset($_POST['interviewer'])){
  echo "Fail";
  exit;
}

$description = $_POST['description'];
$interviewee = $_POST['interviewee'];
$interviewer = $_POST['interviewer'];
$stored = $_POST['stored'];

//Validate first
if(IsInjected($description) || IsInjected($interviewee) || IsInjected($interviewer) || isInjected($stored)) {
    echo "Fail";
    exit;
}

//Send PHPMailer Email
$mail = new PHPMailer;
$mail->isSMTP();
$mail->Host = $config['host'];
$mail->SMTPAuth = $config['SMTPAuth'];
$mail->SMTPSecure = $config['SMTPSecure'];
$mail->Port = $config['port'];
$mail->Username = $config['username'];
$mail->Password = $config['password'];
$mail->setFrom($config['fromAddress'], 'API Data');
$mail->addReplyTo($config['replyAddress'], 'API Data');
$mail->addAddress($config['sendToEmail']);
$mail->Subject = "API :: Data";

if(!isset($_POST['stored'])) {
    $mail->Body = "Description :: {$description}\nInterviewee :: {$interviewee}\nInterviewer :: {$interviewer}\nStored :: {$stored}";
} else {
    $mail->Body = "Description :: {$description}\nInterviewee :: {$interviewee}\nInterviewer :: {$interviewer}";
}




// Send the email
if($mail->send()) {
  echo "Success";
  exit;
} else {
  echo "Fail";
  exit;
}




// Function to validate against any email injection attempts
function IsInjected($str)
{
  $injections = array('(\n+)',
              '(\r+)',
              '(\t+)',
              '(%0A+)',
              '(%0D+)',
              '(%08+)',
              '(%09+)'
              );
  $inject = join('|', $injections);
  $inject = "/$inject/i";
  if(preg_match($inject,$str))
    {
    return true;
  }
  else
    {
    return false;
  }
}
   
?> 