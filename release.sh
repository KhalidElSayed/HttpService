summary() {
  echo ===========================================================================
  echo RELEASE PROCESS
  echo 1 mvn release:prepare release:perform -Plocalrelease
}

intro() {
  clear
  summary
  echo
  echo REQUIREMENTS
  echo localrelease profile
  echo release profile
  echo
  echo Do you want to continue?
  echo 
  read
  clear
}

break() {
  summary
  echo ===========================================================================
  echo
  echo $1 of 1 finished
  echo Do you want to continue?
  read
  clear
}

releasePrepareAndPerform() {
  mvn release:prepare release:perform -Plocalrelease
}

end() {
  summary
  echo
  echo release finished
  echo ===========================================================================
}

intro
releasePrepareAndPerform
break 1
end

