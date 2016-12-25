Infoset Based Taxi System 

#Do-Road

* * *


# Introduction

The Calico Challenge infoset project was originally designed to chart data collected by data gathering programs called agents. An agent was responsible for collecting data of a specific type. For example there were agents for gathering network device statistics, other agents gathered Linux system performance data.

Infoset was designed to be flexible enough to accept data from any number and types of agent. We added a web front end to help persons visualize its capabilites at [http://infoset.palisadoes.org/](http://infoset.palisadoes.org/). At its heart, Infoset is a data collection / retrieval API.

It’s been realized that an agent could be a phone app posting geo-coordinates to the central infoset server. UWI / Utech students have already created a phone app to track geo data, and that there is interest in a tracking application for the transportation system of Jamaica.

The basic aim is to create an app where:

1. Taxi and bus drivers register themselves in the system

2. Passengers register themselves in the system too

3. Passengers will be able to track the status of their favorite route and the proximity of preferred drivers.

4. Operators and passengers will be able to get historical data such as average speeds which could be used to improve the customer experience.

These are long term goals. It is expected that the project will take some time, if not years to become fully operational. This should not limit our expectation to do stealth launches of alpha and beta services.

## Desired Features

The system should have the following features:

1. Possibility of a loyalty system

2. Favorite taxi

3. Passenger registration using the app

4. Driver registration using the app

5. Ability for drivers to state whether they are on or off duty

6. Passengers should be able to know whether:

    1. A taxi on the route is moving towards or away from them

    2. How far away the taxi is

7. Use the existing infoset backend code for data storage

## Technical Features of the Apps

The development would be in multiple phases. We describe the alpha and beta features next:

###  Features for the Alpha

The taxi app should have the following features for the alpha.

#### Taxi Driver

The taxi version of the app should be able to do the following:

1. Data to be sent to the central infoset server via data plan / WiFi to prove the concept. Text messaging communication features can come later.

2. Data to be formatted according to the Infoset’s JSON structure

3. Data need only be sent every 5 minutes. A maximum of 500 bytes per communication.

    1. This would mean a maximum of 12 x 8 x 500 = 250K per day.

4. Data must be stored on the local device in the event of a network failure and sent to the central server when connectivity returns. 

#### Passenger

The passenger version of the app should be able to do the following:

1. Provide the distance from the selected taxi driver phone, its estimated speed and direction.

2. Should filter results to show only the closest taxi phones

3. Should provide this data only on demand, not in real time to reduce data plan usage and server load.

### Features for the Beta

The beta would have the following features

1. A web based server front end API to infoset would have to be developed to do the following:

    1. Driver / Passenger registration using the app

    2. Ability for drivers to state whether they are on or off duty

    3. Username / password login

    4. Accept data requests from registered users.

2. Ability to have short unintrusive messages included in updates. This could be useful for national disasters, and advertising.

We should expect to handle 10,000 taxis (Need confirmation of a realistic number). This would mean 120,000 queries every hour, or 33 transactions per second.

## Why Infoset

Infoset was designed to be an all purpose data gathering application. The web front end was created to help people visualize what could be possible.

JSON data is HTTP posted to the infoset API which then placed in a MySQL based backend. Infoset has the following features:

1. Database connection pooling to reduce the load on the database server when there are high transaction volumes.

2. Multiprocess updates that take advantage of all the CPU cores of a server.

3. Data validation to ensure the JSON meets the specification

# Agents Posting JSON to Infoset

Data from phone agents should post data to the infoset server using json formatted in this way:
```json
{  
   "agent":"taxi",
   "timeseries":{  
      "latitude":{  
         "base_type":1,
         "data":[  
            [  
               1,
               138315134224.0,
               "None"
            ]
         ],
         "description":"Latitude"
      },
      "longitude":{  
         "base_type":1,
         "data":[  
            [  
               1,
               138315134224.0,
               "None"
            ]
         ],
         "description":"Longitude"
      }
   },
   "hostname":"192.168.3.100",
   "timestamp":1474823400,
   "uid":"8a6887228e33e3b433bd0da985c203904a48e2e90804ae217334dde2b905c57e"
}
```
### Agent JSON Structure Explained

<table>
  <tr>
    <td>Field</td>
    <td>Description</td>
  </tr>
  <tr>
    <td>agent</td>
    <td>Agent name</td>
  </tr>
  <tr>
    <td>timeseries</td>
    <td>Time series data follows</td>
  </tr>
  <tr>
    <td>latitude</td>
    <td>Latitude Label. Latitude data follows</td>
  </tr>
  <tr>
    <td>longitude</td>
    <td>Longitude Label. Longitude data follows</td>
  </tr>
  <tr>
    <td>base_type</td>
    <td>Defaults to 1.  Means a non-incremental number.</td>
  </tr>
  <tr>
    <td>description</td>
    <td>Description of the data</td>
  </tr>
  <tr>
    <td>data</td>
    <td>Data related to labels. A list of lists. In the case of the app, this would be a single list of. 
1 = Unique number or string of the data. Ie. In this case we use the number 1 for the first entry.
Value = Value of the data related to the data
Description = Description of the data. This isn’t necessary and is set to None</td>
  </tr>
  <tr>
    <td>hostname</td>
    <td>Hostname of the device sending the data. In the case of a phone, this could be set to None.</td>
  </tr>
  <tr>
    <td>timestamp</td>
    <td>Time when data was generated</td>
  </tr>
  <tr>
    <td>UID</td>
    <td>A unique identifier for the device sending the data. Could be a SIM ID or phone number.</td>
  </tr>
</table>


# Central Database Structure

The central database will need to have the following structure

## Taxi Tables

### Data Table 

<table>
  <tr>
    <td>Field</td>
    <td>Description</td>
  </tr>
  <tr>
    <td>phone</td>
    <td>Phone number of driver</td>
  </tr>
  <tr>
    <td>type</td>
    <td>Transport Type (Taxi/Coaster/JUTC)￼</td>
  </tr>
  <tr>
    <td>Latitude</td>
    <td>Latitude of phone when data was posted</td>
  </tr>
  <tr>
    <td>Longitude</td>
    <td>Longitude of phone when data was posted</td>
  </tr>
  <tr>
    <td>timestamp</td>
    <td>UTC timestamp of when data was posted</td>
  </tr>
</table>


## Infoset Backend Tables

The following describes how the infoset backend works. You can refer to details of each table in infoset’s infoset/db/db_orm.py file.

### Agent Table

<table>
  <tr>
    <td>Field</td>
    <td>Description</td>
  </tr>
  <tr>
    <td>idx</td>
    <td>Index value for the agent</td>
  </tr>
  <tr>
    <td>uid</td>
    <td>Unique identifier for the agent</td>
  </tr>
  <tr>
    <td>name</td>
    <td>Agent name</td>
  </tr>
</table>


### Data Table

<table>
  <tr>
    <td>Field</td>
    <td>Description</td>
  </tr>
  <tr>
    <td>idx_datapoint</td>
    <td>Index value for the datapoint</td>
  </tr>
  <tr>
    <td>value</td>
    <td>Value of the datapoint</td>
  </tr>
  <tr>
    <td>timestamp</td>
    <td>UTC timestamp of when data was posted</td>
  </tr>
</table>


### Datapoint Table

<table>
  <tr>
    <td>Field</td>
    <td>Description</td>
  </tr>
  <tr>
    <td>idx</td>
    <td>The datapoint’s index value</td>
  </tr>
  <tr>
    <td>idx_agent</td>
    <td>Idx value of the agent that created the data</td>
  </tr>
</table>


