#!/bin/bash
iptables -F
iptables -X
iptables -Z

#Accept localhost connetting, no matter what it is
iptables -A INPUT -i lo -j ACCEPT

#Accept any response package which is initiated from inside
iptables -A INPUT -m state --state ESTABLISHED,RELATED -j ACCEPT

#block most common network attacks(recon packets and syn-flood attack)
iptables -A INPUT -p tcp --tcp-flags ALL NONE -j DROP
iptables -A INPUT -p tcp ! --syn -m state --state NEW -j DROP
iptables -A INPUT -p tcp --tcp-flags ALL ALL -j DROP

iptables -A INPUT -p udp --dport 1812 -j ACCEPT #Radius Auth
iptables -A INPUT -p udp --dport 1813 -j ACCEPT #Radius Acc

#default policies
iptables -P OUTPUT ACCEPT
iptables -P INPUT DROP



