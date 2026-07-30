# 2. Hardware and wiring

What this code expects to find on the robot, and the IDs that make it all match. The
authoritative wiring tutorial (with photos) is
[docs.wpilib.org, "Wiring the FRC Control System"](https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-1/intro-to-frc-robot-wiring.html).
Read that alongside this page.

## Bill of materials this project assumes

| Part | Qty | Role |
| --- | --- | --- |
| roboRIO (2.0) | 1 | The robot's computer; runs the code you deploy |
| REV Power Distribution Hub (PDH) | 1 | Splits battery power to everything, with breakers |
| VH-109 radio | 1 | Robot Wi-Fi, which is how the Driver Station talks to it |
| SPARK MAX | 4 | Motor controllers (one per motor) |
| NEO brushless motor | 4 | Drive motors, two per gearbox side |
| Pigeon 2.0 | 1 | Gyro/IMU; tells the code which way the robot faces |
| 12V FRC battery + 120A main breaker | 1 | Power |
| Robot Signal Light (RSL) | 1 | The required orange signal light, wired to the roboRIO |

Chassis: any tank chassis with two driven sides works. The constants default to the KitBot
(AM14U family, Toughbox Mini 10.71:1, 6 inch wheels). If you have a different gearbox or
wheel size, update `GEAR_RATIO` and `WHEEL_DIAMETER_METERS` in `Constants.java`. The
comments there tell you how.

## The CAN bus: one yellow/green daisy chain

Power tells devices that they can run; the CAN bus is how the roboRIO tells them what to
do. It's a single chain of twisted yellow (CAN-H) and green (CAN-L) pairs running from
device to device:

```
roboRIO -- SPARK MAX -- SPARK MAX -- SPARK MAX -- SPARK MAX -- Pigeon 2.0 -- PDH
(start of chain)      (order along the chain doesn't matter)      (end of chain:
                                                          PDH termination switch ON)
```

- Order on the chain is irrelevant. IDs identify devices, not position.
- Both ends must be terminated. The roboRIO terminates its end internally; flip the PDH's
  small CAN termination switch to ON at the other end.
- A single broken CAN wire takes out every device after the break, so crimp carefully and
  zip-tie strain relief everywhere.

## CAN ID table (must match `Constants.java` exactly)

Set these with the REV Hardware Client (SPARK MAXes) and Phoenix Tuner X (Pigeon):

| Device | CAN ID | Constant |
| --- | --- | --- |
| PDH | 1 | (REV default; leave it) |
| Left leader SPARK MAX | 11 | `LEFT_LEADER_CAN_ID` |
| Left follower SPARK MAX | 12 | `LEFT_FOLLOWER_CAN_ID` |
| Right leader SPARK MAX | 13 | `RIGHT_LEADER_CAN_ID` |
| Right follower SPARK MAX | 14 | `RIGHT_FOLLOWER_CAN_ID` |
| Pigeon 2.0 | 20 | `PIGEON_CAN_ID` |

"Leader" versus "follower" is purely a software role, so wire all four identically. Just
make sure the ID you assign matches which side the motor is on. Use the REV client's
"blink" button to confirm which physical controller you're talking to.

## Power wiring summary

- Battery, then main breaker, then the PDH's big terminals. That's the only high-current
  path.
- Each SPARK MAX gets its own PDH channel with a 40A breaker, wired in 12 AWG.
- The roboRIO connects to the PDH's dedicated non-breaker 10A port. The radio connects to
  the PDH's regulated radio port (the VH-109 takes PoE or barrel; see the radio guide).
- The Pigeon 2.0 draws very little, so any low-current PDH channel works, plus the CAN
  chain.
- A NEO's three thick phase wires AND its thin 6-pin encoder cable both go from the motor
  to its SPARK MAX. A NEO without its encoder cable will stutter or refuse to spin: it's a
  brushless motor, and the controller needs that sensor.

## Before first power-on

- [ ] Every red/black polarity double-checked (reversed power destroys electronics)
- [ ] No stray wire strands at any terminal; tug-test every crimp
- [ ] CAN chain continuous, PDH termination ON
- [ ] Wheels off the ground on blocks (the code will move them)
- [ ] Battery charged, main breaker accessible, everyone knows where Disable is
