<?php
function right_panel($name)
{
?>
<!-- ride side of topnav bar -->
            <div class="pull-right">
                <ul class="nav pull-right">
                    <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">Welcome, <?php echo "$name"; ?> <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="change_password.php"><i class="icon-cog"></i> Change Password</a></li>
                            <!--<li><a href="#"><i class="icon-envelope"></i> Contact Support</a></li>-->
                            <li class="divider"></li>
                            <li><a href="logout.php"><i class="icon-off"></i> Logout</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
<?php
}

function left_panel($entries, $userID, $Site_Name,$userAdmin, $site)
{
?>
<a class="brand" href="<?php echo "$site"; ?>"><?php echo "$Site_Name"; ?></a>
        <div class="nav-collapse collapse">
            <ul class="nav">
				<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">Entries<b class="caret"></b></a>
                        <ul class="dropdown-menu">
							 <?php
								$count = 0;
								while ($count < $entries) {
									$num = $count + 1;
									$entryID = $userID . $num;
									echo "<li><a href='index.php?entryID=$entryID'><i class='icon-cog'></i> Entry $num</a></li>";
									$count++;
								}
							?>
                        </ul>
                </li>
				<li><a href="results.php" >Results</a></li>
				<li><a href="rules.php" >Rules</a></li>
				<li><a href="faq.php" >FAQs</a></li>
				<li><a href="tutorial.pdf" >Tutorial</a></li>
				<?php if ($userAdmin) {  echo "<li><a href=admin/index.php>Admin</a></li>"; } ?>
            </ul>
		</div><!--/.nav-collapse -->
<?php
}
?>