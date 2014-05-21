<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <!-- <link rel="shortcut icon" href="http://getbootstrap.com/assets/ico/favicon.ico"> -->
    <title>Music Manager</title>

    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="bootstrap/css/jumbotron.css" rel="stylesheet">

  	<style type="text/css"></style>
</head>
<body>
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="index.html">Music Manager</a>
        </div>
        <div class="navbar-collapse collapse">
          <form class="navbar-form navbar-right" role="form">
            <div class="form-group">
              <input type="text" placeholder="Username" class="form-control">
            </div>
            <div class="form-group">
              <input type="password" placeholder="Password" class="form-control">
            </div>
            <button type="submit" class="btn btn-success">Sign in</button>
            <button type="submit" class="btn btn-primary">Sign up</button>
          </form>
        </div>
      </div>
    </div>

    <div class="jumbotron">
      <div class="container">
        <h1>Share your noises</h1>
        <p>Explore the smallest community</br>of artists, amateur DJ's, podcasters</br>and pirates.</p>
        <p>Not a member? Sign up already! It's free.</p>
        <p><a class="btn btn-primary btn-lg" role="button">Sign up</a></p>
      </div>
    </div>
	
	
    
    <div class="jumbotron">
    <div class="container">
      <h2>Terms of Service</h2>
      <p>You are responsible for your use of Music Manager, 
      for any Content you post to Music Manager, and for any consequences thereof.
      The Content you submit, post, or display will be able to be viewed by other
      users of Music Manager, therefore Music Manager cannot guarantee the confidentiality of any Content.</br></br>
      Music Manager and it's authors disagree with any Content posted and expressly disclaims any liability related to the Content.</br></br>
      You should only provide Content that you are comfortable sharing with others
      under these Terms.</p></br></br>
      <h3>Don't Cross the Line</h3>
      <p>Respect copyright. Only upload sounds that you made or that you are authorized to use.</br></br>
      We encourage free speech and defend everyone's right to express unpopular points of view.
      But we don't permit hate speech (speech which attacks or demeans a group based on race or
       ethnic origin, religion, disability, gender, age, veteran status, and sexual orientation/gender
       identity).</p>
       <h3>Don't Hate Us</h3>
       <p>Music Manager and it's authors expresses the right to go unpunished in case of prosecution because of some missing terms,
       horrible content posted by horrible authors and so on.
       </p>
      <hr>
      </div>
    </div>
    
    <div class="container">
      <footer>
        <p>© 2014 Daniel Sák, Filip Halas, Radek Koubský, Roman Mačor</p>
      </footer>
	</div> <!-- /container -->
<script src="./Jumbotron Template for Bootstrap_files/jquery.min.js"></script>
<script src="./Jumbotron Template for Bootstrap_files/bootstrap.min.js"></script>
</body>
</html>