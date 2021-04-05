# NamOps-Container-Drivers-Portal

- This is a custom Android app for NamOps Logistics. 

- It's purpose is to monitor and control the lifecycle of the task of <ins>picking up and delivering containers</ins> by truck drivers at NamOps.
- Each tasks goes through the following phases:

1. Admin create a job-card and push it to the backend via an admin console -[not part of this app] each job card can have multiple containers to be picked up
2. App read the job card items from the backend and display to the drivers - some jobcard items might be pre-assigned to specific drivers
3. Driver creates a trip, depending on the instruction on the jobcard:
4. Based on the instructions on the job card - the app guides the driver on the next sub-task to do during the trip.
5. Sub- task can be: 
  * Weigh Empty truck - with no container
  * Pick up the container
  * Weigh loaded truck - with the container
  * Thermally Scan the container
  * Drop off container
  * Trip is completed  -driver can now create another trip

This way the admin can control what the driver did in a day.
