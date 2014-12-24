[BITS 32]

xor eax, eax
add eax, ecx
add eax, edx
push ebp
mov ebp, esp
push edx
push ecx
mov eax, 041424344h
mov edx, dword [ ebp + 08h]
mov ecx, dword [ ebp + 0ch]
add ecx, edx
mov eax, ecx
pop edx
pop ecx
pop ebp
retn 08h
